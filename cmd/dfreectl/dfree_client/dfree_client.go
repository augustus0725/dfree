package dfree_client

import (
	"fmt"
	"github.com/go-resty/resty/v2"
	"github.com/jedib0t/go-pretty/v6/table"
)

type DfreeClient struct {
	Client        *resty.Client
	DaemonAddress string
	TableWriter   table.Writer
}

type CreateNamespace struct {
	Namespace string `json:"namespace"`
}

type CreateNamespaceResponse struct {
	Success bool `json:"success"`
}

func (dc DfreeClient) CreateNamespace(namespace string) {
	result := &CreateNamespaceResponse{}
	_, err := dc.Client.R().
		SetBody(&CreateNamespace{
			Namespace: namespace,
		}).SetResult(result).
		Put(dc.DaemonAddress + "/dfree-daemon/api/v1/namespace")
	if err != nil && !result.Success {
		println(fmt.Sprintf("create namespace %s fail", namespace))
		return
	}
	println(fmt.Sprintf("create namespace %s success", namespace))
}

type ListNamespacesResponse struct {
	Data    []string `json:"data"`
	Success bool     `json:"success"`
}

func (dc DfreeClient) ListNamespaces() {
	result := &ListNamespacesResponse{}
	_, err := dc.Client.R().SetResult(result).Get(dc.DaemonAddress + "/dfree-daemon/api/v1/namespaces")
	if err != nil && !result.Success {
		println("query namespaces fail")
		return
	}
	// print title
	dc.TableWriter.AppendHeader(table.Row{"NAME", "STATUS", "AGE"})
	// print data
	for _, v := range result.Data {
		dc.TableWriter.AppendRow(table.Row{v, "", ""})
	}
	dc.TableWriter.Render()
}

type DeleteNamespace struct {
	Namespace string `json:"namespace"`
}

type DeleteNamespaceResponse struct {
	Success bool `json:"success"`
}

func (dc DfreeClient) DeleteNamespace(namespace string) {
	result := &DeleteNamespaceResponse{}
	_, err := dc.Client.R().SetBody(&DeleteNamespace{
		Namespace: namespace,
	}).SetResult(result).Delete(dc.DaemonAddress + "/dfree-daemon/api/v1/namespace")
	if err != nil {
		println(fmt.Sprintf("delete namespace %s fail", namespace))
		return
	}
	println(fmt.Sprintf("delete namespace %s success", namespace))
}

func (dc DfreeClient) DescribeNamespace(namespace string) {

}

func (dc DfreeClient) DescribeInstance(instance string, namespace string) {

}

type Template struct {
	Name        string `json:"name"`
	Description string `json:"description"`
	Timestamp   string `json:"timestamp"`
}

type ListTemplatesResponse struct {
	Success bool       `json:"success"`
	Data    []Template `json:"data"`
}

func (dc DfreeClient) ListTemplates() {
	result := &ListTemplatesResponse{}
	_, err := dc.Client.R().SetResult(result).Get(dc.DaemonAddress + "/dfree-daemon/api/v1/templates")
	if err != nil && !result.Success {
		println("query templates fail")
		return
	}
	// title
	dc.TableWriter.AppendHeader(table.Row{"NAME", "DESCRIPTION", "TIMESTAMP"})
	// data
	for _, v := range result.Data {
		dc.TableWriter.AppendRow(table.Row{v.Name, v.Description, v.Timestamp})
	}
	dc.TableWriter.Render()
}

func (dc DfreeClient) Apply(resource string) {

}

func (dc DfreeClient) GetInstances(namespace string) {

}

func (dc DfreeClient) LogsFInstance(namespace string, instance string, follow bool) {

}
