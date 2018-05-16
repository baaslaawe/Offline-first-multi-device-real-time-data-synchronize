package controllers

import (
	"net/http"
	"github.com/labstack/echo"
)

type SyncApiController struct {
}

func (SyncApiController) SyncLocal(c echo.Context) error {

	return ReturnApiSucc(c, http.StatusOK, ArrayResultTotal{
	})
}

func (SyncApiController) SyncServer(c echo.Context) error {

	return ReturnApiSucc(c, http.StatusOK, ArrayResultTotal{
	})
}
