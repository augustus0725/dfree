package main

import (
	"bufio"
	"bytes"
	"dfree/cmd/dfreectl/dfree_client"
	yaml2 "dfree/pkg/yaml"
	"errors"
	"fmt"
	"github.com/go-resty/resty/v2"
	"github.com/jedib0t/go-pretty/v6/table"
	"gopkg.in/alecthomas/kingpin.v2"
	"gopkg.in/yaml.v3"
	"os"
	"os/exec"
	"os/user"
	"path"
	"runtime"
	"strings"
)

var (
	app = kingpin.New("dfreectl", "Manage dfree task.")

	apply        = app.Command("apply", "Apply for a resource.")
	resourceFlag = apply.Flag("file", "Resource file").Short('f').Required().String()

	get             = app.Command("get", "Query the state of all kinds of resources.")
	getNamespace    = get.Command("namespaces", "get all namespaces")
	getInstance     = get.Command("instance", "get all instances")
	getInstanceFlag = getInstance.Flag("namespace", "filter by namespace").Short('n').Required().String()
	getTemplate     = get.Command("templates", "get all templates")

	start                  = app.Command("start", "Start instance")
	startInstanceNamespace = start.Flag("namespace", "instance namespace").Short('n').Required().String()
	startInstance          = start.Arg("instance name", "The instance we will start.").Required().String()

	stop                  = app.Command("stop", "stop instance")
	stopInstanceNamespace = stop.Flag("namespace", "instance namespace").Short('n').Required().String()
	stopInstance          = stop.Arg("instance name", "The instance we will stop.").Required().String()

	create             = app.Command("create", "create some resource")
	createNamespace    = create.Command("namespace", "create a namespace")
	createNamespaceArg = createNamespace.Arg("namespace name", "namespace name").Required().String()

	deleteCommand      = app.Command("delete", "delete some resource")
	deleteNamespace    = deleteCommand.Command("namespace", "delete a namespace")
	deleteNamespaceArg = deleteNamespace.Arg("namespace name", "namespace name").Required().String()

	describe             = app.Command("describe", "Describe the resource.")
	describeNamespace    = describe.Command("namespace", "get more information about this namespace")
	describeNamespaceArg = describeNamespace.Arg("a namespace name", "a namespace name").Required().String()
	describeInstance     = describe.Command("instance", "get more information about this instance")
	describeInstanceFlag = describeInstance.Flag("namespace", "filter by namespace").Short('n').Required().String()
	describeInstanceArg  = describeInstance.Arg("a instance name", "a instance name").Required().String()

	logs                = app.Command("logs", "Output logs of a runnable resource.")
	follow              = logs.Flag("follow", "output appended data as the file grows").Short('f').Default("false").Bool()
	namespaceOfInstance = logs.Flag("namespace", "which namespace this instance belongs to").Short('n').Default("default").String()
	logInstanceName     = logs.Arg("target", "Get log from the target resource.").Required().String()
)

type Config struct {
	DaemonAddress string `yaml:"daemonAddress"`
}

var dfreeDaemonAddress string

func main() {
	err := detectConfig()
	if err != nil {
		return
	}
	tableWriter := table.NewWriter()
	tableWriter.SetOutputMirror(os.Stdout)
	dc := dfree_client.DfreeClient{
		Client:        resty.New(),
		DaemonAddress: dfreeDaemonAddress,
		TableWriter:   tableWriter,
	}

	switch kingpin.MustParse(app.Parse(os.Args[1:])) {
	case apply.FullCommand():
		dc.Apply(*resourceFlag)
	case getInstance.FullCommand():
		dc.GetInstances(*getInstanceFlag)
	case getNamespace.FullCommand():
		dc.ListNamespaces()
	case createNamespace.FullCommand():
		dc.CreateNamespace(*createNamespaceArg)
	case deleteNamespace.FullCommand():
		dc.DeleteNamespace(*deleteNamespaceArg)
	case describeNamespace.FullCommand():
		dc.DescribeNamespace(*describeNamespaceArg)
	case describeInstance.FullCommand():
		dc.DescribeInstance(*describeInstanceArg, *describeInstanceFlag)
	case logs.FullCommand():
		dc.LogsFInstance(*namespaceOfInstance, *logInstanceName, *follow)
	case getTemplate.FullCommand():
		dc.ListTemplates()
	case start.FullCommand():
		dc.StartInstance(*startInstanceNamespace, *startInstance)
	case stop.FullCommand():
		dc.StopInstance(*stopInstanceNamespace, *stopInstance)
	}
}

func detectConfig() error {
	home, err := home()
	if err != nil {
		return err
	}
	dfreeConfDir := path.Join(home, ".dfree")
	if _, err := os.Stat(dfreeConfDir); os.IsNotExist(err) {
		err := os.Mkdir(dfreeConfDir, os.ModePerm)
		if err != nil {
			return err
		}
	}
	dfreeConf := path.Join(dfreeConfDir, "dfree.conf")
	config := Config{
		DaemonAddress: "http://127.0.0.1:7856",
	}
	if _, err := os.Stat(dfreeConf); os.IsNotExist(err) {
		reader := bufio.NewReader(os.Stdin)
		dfreeConfFile, err := os.Create(dfreeConf)
		if err != nil {
			return err
		}
		defer func(dfreeConfFile *os.File) {
			err := dfreeConfFile.Close()
			if err != nil {
				// ignore
			}
		}(dfreeConfFile)
		print(fmt.Sprintf("%s not config, use default dfree-daemon address(http://127.0.0.1:7856)? Y/N:", dfreeConf))
		yesOrNo, err := reader.ReadString('\n')
		if err != nil {
			return err
		}
		yesOrNoTrimed := strings.TrimSpace(yesOrNo)
		if yesOrNoTrimed == "yes" || yesOrNoTrimed == "y" || yesOrNoTrimed == "Y" {
			configBytes, err := yaml.Marshal(&config)
			if err != nil {
				return err
			}

			_, err = dfreeConfFile.Write(configBytes)
			if err != nil {
				return err
			}
			dfreeDaemonAddress = "http://127.0.0.1:7856"
			return nil
		} else {
			print("Please set dfree daemon address(e.g.: http://127.0.0.1:7856):")
			daemonAddress, err := reader.ReadString('\n')
			if err != nil {
				return err
			}
			// TODO check valid for daemonAddress
			dfreeDaemonAddress = strings.Trim(daemonAddress, "\r\n ")
			config.DaemonAddress = dfreeDaemonAddress
			configBytes, err := yaml.Marshal(&config)
			if err != nil {
				return err
			}
			_, err = dfreeConfFile.Write(configBytes)
			if err != nil {
				return err
			}
		}
	} else {
		err = yaml2.Unmarshal(dfreeConf, &config)
		if err != nil {
			return err
		}
		dfreeDaemonAddress = config.DaemonAddress
	}

	return nil
}

func home() (string, error) {
	currentUser, err := user.Current()
	if nil == err {
		return currentUser.HomeDir, nil
	}

	// cross compile support

	if "windows" == runtime.GOOS {
		return homeWindows()
	}

	// Unix-like system, so just assume Unix
	return homeUnix()
}

func homeUnix() (string, error) {
	// First prefer the HOME environmental variable
	if home := os.Getenv("HOME"); home != "" {
		return home, nil
	}

	// If that fails, try the shell
	var stdout bytes.Buffer
	cmd := exec.Command("sh", "-c", "eval echo ~$USER")
	cmd.Stdout = &stdout
	if err := cmd.Run(); err != nil {
		return "", err
	}

	result := strings.TrimSpace(stdout.String())
	if result == "" {
		return "", errors.New("blank output when reading home directory")
	}

	return result, nil
}

func homeWindows() (string, error) {
	drive := os.Getenv("HOMEDRIVE")
	path := os.Getenv("HOMEPATH")
	home := drive + path
	if drive == "" || path == "" {
		home = os.Getenv("USERPROFILE")
	}
	if home == "" {
		return "", errors.New("HOMEDRIVE, HOMEPATH, and USERPROFILE are blank")
	}

	return home, nil
}
