/*

 ```
 $ sudo npm run dev

 ```

 */

var express = require('express');
var app = express();
var port = process.env.PORT || 3000;

var morgan = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var session = require('express-session');
var path = require('path');

var db = require('./../app/back_end/util/db_pool.js');
var helpers = require('./../app/back_end/util/helpers.js');

//  set up the express application
app.use(morgan('dev'));                               //  log every request to the console
app.use(cookieParser());                              //  read cookies (needed for auth)
app.use(bodyParser.json({ limit: "50mb" }));
app.use(bodyParser.urlencoded({ limit: "50mb", extended: true, parameterLimit: 50000 }));
app.engine('html', require('ejs').renderFile);
app.set('view engine', 'html');

/**
 * webpack setting
 */

app.use(express.static(path.join(__dirname, '../')));

/*
 * request routing
 */
require('./../app/back_end/routes/mainAPI.js')(app);
require('./../app/back_end/routes/syncAPI.js')(app);

/*
 * 404 page
 */
app.get('*', fourOhFour);
function fourOhFour (req, res) {
    res.writeHead(404, { "Content-Type": "application/json" });
    res.end(JSON.stringify(helpers.invalidResource()) + "\n");
}

/*
 * start db & express server start
 */
db.init(function (err) {
    if (err) {
        console.log("Fatal error during initializing Database: ");
        console.log(err);
    }
    else {
        app.listen(port);
        console.log('Now, the server starts on port ' + port);
    }
});

module.exports = app;
