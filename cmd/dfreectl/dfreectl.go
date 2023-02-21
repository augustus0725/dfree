package main

import (
	"gopkg.in/alecthomas/kingpin.v2"
	"os"
	"strconv"
)

var (
	app = kingpin.New("dfreectl", "Manage dfree task.")

	apply        = app.Command("apply", "Apply for a resource.")
	resourceFlag = apply.Flag("file", "Resource file").Short('f').Required().String()

	get             = app.Command("get", "Query the state of all kinds of resources.")
	getNamespace    = get.Command("namespaces", "get all namespaces")
	getInstance     = get.Command("instance", "get all instances")
	getInstanceFlag = getInstance.Flag("namespace", "filter by namespace").Short('n').Required().String()

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
	describeInstanceArg  = describeInstance.Arg("a instance name", "a instance name").Required().String()

	logs   = app.Command("logs", "Output logs of a runnable resource.")
	follow = logs.Flag("follow", "output appended data as the file grows").Short('f').Default("false").Bool()
	target = logs.Arg("target", "Get log from the target resource.").Required().String()
)

func main() {
	switch kingpin.MustParse(app.Parse(os.Args[1:])) {
	case apply.FullCommand():
		println("apply: " + *resourceFlag)
	case getInstance.FullCommand():
		println("get instance, namespace is : " + *getInstanceFlag)
	case getNamespace.FullCommand():
		println("get: ")
	case createNamespace.FullCommand():
		println("create: " + *createNamespaceArg)
	case deleteNamespace.FullCommand():
		println("delete:" + *deleteNamespaceArg)
	case describeNamespace.FullCommand():
		println("describe: " + *describeNamespaceArg)
	case describeInstance.FullCommand():
		println("describe: " + *describeInstanceArg)
	case logs.FullCommand():
		println("logs: " + strconv.FormatBool(*follow) + "->" + *target)
	}
}
