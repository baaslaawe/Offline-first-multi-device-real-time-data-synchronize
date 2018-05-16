package main

import (
	"flag"
	"fmt"
	"log"
	"os"
	"runtime"

	"github.com/labstack/echo"
	"github.com/labstack/echo/middleware"

	"github.com/asaskevich/govalidator"

	"github.com/go-xorm/xorm"
	_ "github.com/go-sql-driver/mysql"

	configutil "github.com/pangpanglabs/goutils/config"
	"github.com/pangpanglabs/goutils/echomiddleware"

	"./router"
	"./models"
)

/*

 ssh -i /Users/moka/Dropbox/morningcafe/moka.pem ubuntu@52.79.139.41
 점속후,
 git pull 받는다

 === 기존에 떠있는 있떤거를 먼저 삭제후
 Check list
 $ ps xw

 Kill
 $ kill PID




 === Start Server
 $ nohup go run main.go -app-env=product &


 === Start Telegram Bot Polling
 $ nohup go run pollingTelegram.go -app-env=product &

 */

func main() {
	/*
	command line 에서 parameter 를 불러온다
	$ go run main.go -app-env=dev
	$ go run main.go -app-env=product
	*/
	appEnv := flag.String("app-env", os.Getenv("APP_ENV"), "app env")
	flag.Parse()

	/* Setting config from config.yml */
	var c Config
	if err := configutil.Read(*appEnv, &c); err != nil {
		panic(err)
	}
	fmt.Println(c)

	/* Init database */
	db, err := initDB(c.Database.Driver, c.Database.Connection)
	if err != nil {
		panic(err)
	}
	defer db.Close()

	/* Init echo app */
	e := echo.New()

	router.Init(e, *appEnv)

	e.Static("/static", "static")
	e.Pre(middleware.RemoveTrailingSlash())
	e.Use(middleware.Recover())
	e.Use(middleware.CORS())
	e.Use(middleware.Logger())
	e.Use(middleware.RequestID())
	e.Use(echomiddleware.ContextLogger())

	e.Validator = &Validator{}
	e.Debug = c.Debug

	/* Start server */
	if err := e.Start(":" + c.HttpPort); err != nil {
		log.Println(err)
	}
}

func initDB(driver, connection string) (*xorm.Engine, error) {
	db, err := xorm.NewEngine(driver, connection)
	if err != nil {
		return nil, err
	}

	if driver == "sqlite3" {
		runtime.GOMAXPROCS(1)
	}

	db.Sync(new(models.Question))
	db.Sync(new(models.Answer))

	return db, nil
}

type Config struct {
	Database struct {
		Driver     string
		Connection string
	}

	Debug    bool
	Service  string
	HttpPort string
}

type Validator struct{}

func (v *Validator) Validate(i interface{}) error {
	_, err := govalidator.ValidateStruct(i)
	return err
}
