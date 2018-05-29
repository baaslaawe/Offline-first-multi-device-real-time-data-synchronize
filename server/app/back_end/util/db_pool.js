var mysql = require('mysql');
var pool = require('generic-pool');
var local = require("../../../config/local.config.js");

var mysql_pool;

exports.init = function (callback) {

    //  create mysql connection pool to be used
    var dbconfig = local.config.db_config;
    mysql_pool = pool.Pool({
        name: 'mysql',
        create: function (callback) {
            var connection = mysql.createConnection({
                host: dbconfig.host,
                user: dbconfig.user,
                port: dbconfig.port,
                password: dbconfig.password,
                database: dbconfig.database
            });
            callback(null, connection);
        },
        destroy: function (client) {
            client.end();
        },
        max: dbconfig.pooled_connections,
        idleTimeoutMillis: dbconfig.idle_timeout_millis,
        log: false
    });

    //  check whether it is working
    exports.query("SELECT 1", [], function (err, results) {

        if (err != null) {

            callback(err);
            console.error("Unable to connect to the database server. Aborting.");
        }
        else {

            console.log("Database initialized and connected.");
            callback(null);
        }
    });
};

//  query example. not used in CASTME (just for reference)
exports.query = function (query, values, callback) {

    mysql_pool.acquire(function (err, connection) {

        connection.query(query, values, function (mysqlerr, mysqlresults) {

            mysql_pool.release(connection);
            callback(mysqlerr, mysqlresults);
        });
    });
};

//  get connection method
exports.db = function (callback) {

    mysql_pool.acquire(callback);
};

//  release the pool
exports.release = function (client) {

    mysql_pool.release(client);
};
