package yaml

import (
	"github.com/stretchr/testify/require"
	"testing"
)

type Demo struct {
	ApiVersion string `yaml:"apiVersion"`
	Kind       string `yaml:"kind"`
}

func TestUnmarshal(t *testing.T) {
	demo := Demo{}
	err := Unmarshal("testdata/instance-demo.yaml", &demo)
	require.Nil(t, err)
	require.Equal(t, "apps/v1", demo.ApiVersion)
	require.Equal(t, "DfreeInstance", demo.Kind)
}
