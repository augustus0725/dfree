package dfree_client

import (
	"dfree/cmd/dfreectl/dfree_resource"
	"dfree/pkg/yaml"
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

type CommonResource struct {
	ApiVersion string `yaml:"apiVersion"`
	Kind       string `yaml:"kind"`
}

type ScheduleStrategy struct {
	SchedulingStrategy string `json:"schedulingStrategy"`
	SchedulingPeriod   string `json:"schedulingPeriod"`
}

type CreateInstance struct {
	Namespace string `json:"namespace"`
	Instance  string `json:"instance"`
	Template  string `json:"template"`

	Properties       map[string]map[string]string `json:"properties"`
	ScheduleStrategy ScheduleStrategy             `json:"scheduleStrategy"`
}

type CreateInstanceResponse struct {
	Success bool   `json:"success"`
	Data    string `json:"data"`
}

func (dc DfreeClient) Apply(resource string) {
	cr := CommonResource{}
	err := yaml.Unmarshal(resource, &cr)
	if err != nil {
		return
	}
	if "apps/v1" == cr.ApiVersion && "DfreeInstance" == cr.Kind {
		dfv1 := dfree_resource.DfreeInstanceV1{}
		err = yaml.Unmarshal(resource, &dfv1)
		if err != nil {
			return
		}
		// create dfree instance
		result := CreateInstanceResponse{}
		_, err := dc.Client.R().SetBody(&CreateInstance{
			Namespace:  dfv1.Metadata.Namespace,
			Instance:   dfv1.Metadata.Name,
			Template:   dfv1.Spec.Instance.Template,
			Properties: dfv1.Spec.Instance.Properties,
			ScheduleStrategy: ScheduleStrategy{
				SchedulingStrategy: dfv1.Spec.Instance.ScheduleStrategy.SchedulingStrategy,
				SchedulingPeriod:   dfv1.Spec.Instance.ScheduleStrategy.SchedulingPeriod,
			},
		}).SetResult(result).Put(dc.DaemonAddress + "/dfree-daemon/api/v1/instance")
		if err != nil && !result.Success {
			println(fmt.Sprintf("apply resource : %s fail to create instance: %s from template: %s",
				resource, dfv1.Metadata.Name, dfv1.Spec.Instance.Template))
			return
		}
		println(fmt.Sprintf("apply resource : %s success", resource))
	}
}

func (dc DfreeClient) GetInstances(namespace string) {

}

func (dc DfreeClient) LogsFInstance(namespace string, instance string, follow bool) {

}

type StartInstance struct {
	Namespace string `json:"namespace"`
	Instance  string `json:"instance"`
}

type StartInstanceResponse struct {
	Success bool `json:"success"`
}

func (dc DfreeClient) StartInstance(namespace string, instance string) {
	result := StartInstanceResponse{}
	_, err := dc.Client.R().SetBody(&StartInstance{
		Namespace: namespace,
		Instance:  instance,
	}).SetResult(result).Put(dc.DaemonAddress + "/dfree-daemon/api/v1/instance/start")
	if err != nil && !result.Success {
		println(fmt.Sprintf("start namespace: %s instance: %s fail", namespace, instance))
		return
	}
	println(fmt.Sprintf("start namespace: %s instance: %s success", namespace, instance))
}

type StopInstance struct {
	Namespace string `json:"namespace"`
	Instance  string `json:"instance"`
}

type StopInstanceResponse struct {
	Success bool `json:"success"`
}

func (dc DfreeClient) StopInstance(namespace string, instance string) {
	result := StopInstanceResponse{}
	_, err := dc.Client.R().SetBody(&StopInstance{
		Namespace: namespace,
		Instance:  instance,
	}).SetResult(result).Put(dc.DaemonAddress + "/dfree-daemon/api/v1/instance/stop")
	if err != nil && !result.Success {
		println(fmt.Sprintf("stop namespace: %s instance: %s fail", namespace, instance))
		return
	}
	println(fmt.Sprintf("stop namespace: %s instance: %s success", namespace, instance))
}
