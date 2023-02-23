package dfree_resource

import (
	"github.com/stretchr/testify/require"
	"gopkg.in/yaml.v3"
	"io"
	"os"
	"testing"
)

func TestInstanceV1(t *testing.T) {
	fDemo, _ := os.Open("testdata/instance-demo.yaml")
	defer func(fDemo *os.File) {
		_ = fDemo.Close()
	}(fDemo)
	fDemoBytes, _ := io.ReadAll(fDemo)

	df := DfreeInstanceV1{}
	err := yaml.Unmarshal(fDemoBytes, &df)
	require.Nil(t, err)
	require.Equal(t, "apps/v1", df.ApiVersion)
	require.Equal(t, "DfreeInstance", df.Kind)
	require.Equal(t, "helloinstance", df.Metadata.Name)
	require.Equal(t, "dev", df.Metadata.Namespace)
	require.Equal(t, "LogFile", df.Spec.Instance.Template)
	require.Equal(t, "CRON_DRIVEN", df.Spec.Instance.ScheduleStrategy.SchedulingStrategy)
	require.Equal(t, "* * * * * ?", df.Spec.Instance.ScheduleStrategy.SchedulingPeriod)
	require.Equal(t, "/opt/getfile/001", df.Spec.Instance.Properties["dfree_getfile_v01"]["Input Directory"])
}
