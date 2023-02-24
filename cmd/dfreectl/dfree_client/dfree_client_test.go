package dfree_client

import (
	"bytes"
	"encoding/json"
	"github.com/go-resty/resty/v2"
	"github.com/jedib0t/go-pretty/v6/table"
	"net/http"
	"os"
	"testing"
)

func createDfreeClient() *DfreeClient {
	daemonAddress := "127.0.0.1:7856"
	tableWriter := table.NewWriter()
	dfreeClient := DfreeClient{
		Client:        resty.New(),
		DaemonAddress: "http://" + daemonAddress,
		TableWriter:   tableWriter,
	}
	tableWriter.SetOutputMirror(os.Stdout)
	return &dfreeClient
}

func TestRawHttp(t *testing.T) {
	c := &http.Client{}
	payload, err := json.Marshal(map[string]interface{}{
		"namespace": "k01",
	})
	if err != nil {
		return
	}
	req01, _ := http.NewRequest(http.MethodPut, "http://127.0.0.1:7856/dfree-daemon/api/v1/namespace", bytes.NewBuffer(payload))
	req01.Header.Add("Content-Type", "application/json")
	response, err := c.Do(req01)
	if err != nil {
		return
	}
	println(response)
}

func TestCreateNamespace(t *testing.T) {
	dc := createDfreeClient()

	dc.CreateNamespace("k01")
}

func TestListNamespaces(t *testing.T) {
	dc := createDfreeClient()

	dc.ListNamespaces()
}

func TestDeleteNamespace(t *testing.T) {
	dc := createDfreeClient()

	dc.DeleteNamespace("k01")
}

func TestListTemplates(t *testing.T) {
	dc := createDfreeClient()

	dc.ListTemplates()
}

func TestApply(t *testing.T) {
	dc := createDfreeClient()

	dc.Apply("testdata/instance-demo.yaml")
}

func TestStartInstance(t *testing.T) {
	dc := createDfreeClient()

	dc.StartInstance("ods", "helloinstance")
}

func TestStopInstance(t *testing.T) {
	dc := createDfreeClient()

	dc.StopInstance("ods", "helloinstance")
}
