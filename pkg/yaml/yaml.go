package yaml

import (
	"gopkg.in/yaml.v3"
	"io"
	"os"
)

func Unmarshal(path string, out interface{}) (err error) {
	f, err := os.Open(path)
	defer func(f *os.File) {
		err := f.Close()
		if err != nil {
			// ignore
		}
	}(f)
	if err != nil {
		return err
	}
	fAllBytes, err := io.ReadAll(f)
	if err != nil {
		return err
	}
	err = yaml.Unmarshal(fAllBytes, out)
	if err != nil {
		return err
	}
	return nil
}
