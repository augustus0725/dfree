package main

import (
	"github.com/stretchr/testify/require"
	"testing"
)

func TestHome(t *testing.T) {
	home, err := Home()
	require.True(t, err == nil)
	require.NotNil(t, home)
}
