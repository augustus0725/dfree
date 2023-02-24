package dfree_resource

type Metadata struct {
	Name      string            `yaml:"name"`
	Namespace string            `yaml:"namespace"`
	Labels    map[string]string `yaml:"labels"`
}

type ScheduleStrategy struct {
	SchedulingStrategy string `yaml:"schedulingStrategy"`
	SchedulingPeriod   string `yaml:"schedulingPeriod"`
}

type TemplateInstance struct {
	Template         string                       `yaml:"template"`
	ScheduleStrategy ScheduleStrategy             `yaml:"scheduleStrategy"`
	Properties       map[string]map[string]string `yaml:"properties"`
}

type Spec struct {
	Instance TemplateInstance `yaml:"instance"`
}

type DfreeInstanceV1 struct {
	ApiVersion string   `yaml:"apiVersion"`
	Kind       string   `yaml:"kind"`
	Metadata   Metadata `yaml:"metadata"`
	Spec       Spec     `yaml:"spec"`
}
