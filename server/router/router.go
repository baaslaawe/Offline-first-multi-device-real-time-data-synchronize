package router

import (
	"github.com/labstack/echo"

	"../controllers"
)

func Init(e *echo.Echo, appEnv string) {
	e.File("/", "views/index.html")

	sync := e.Group("/api/sync")
	sync.GET("/local", controllers.SyncApiController{}.SyncLocal)
	sync.POST("/server", controllers.SyncApiController{}.SyncServer)
}
