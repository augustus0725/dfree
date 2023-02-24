package main

import (
	"github.com/stretchr/testify/require"
	"testing"
)

func TestHome(t *testing.T) {
	homeDir, err := home()
	require.True(t, err == nil)
	require.NotNil(t, homeDir)
}
