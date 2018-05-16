package controllers

import (
	"net/http"

	"github.com/labstack/echo"
)

type HomeController struct {
}

func (HomeController) Get(c echo.Context) error {
	return c.Render(http.StatusOK, "index", nil)
}
