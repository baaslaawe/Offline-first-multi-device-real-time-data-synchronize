/**
 * about making db query
 */
var mysql = require('mysql');
var async = require('async');

var local = require("../../../config/local.config.js");
var helper = require("./helpers.js");
var db = require("./db_pool.js");


exports.query = function (queryString, values, callback) {
    var dbc;

    var queryTest = queryString;
    queryTest = queryTest.replace(/\?/g, "dieguen!hye");

    for (var i = 0; i < values.length; i++)
        queryTest = queryTest.replace("dieguen!hye", values[i]);

    console.log("***** query is : " + queryTest);

    async.waterfall([
            function (cb) {
                db.db(cb);
            },
            function (dbclient, cb) {
                dbc = dbclient;
                dbc.query(queryTest, cb);
            },
            function (rows, fields, cb) {
                if (!rows || rows.length == 0)
                    cb(helper.noSuchRow());
                else
                    cb(null, rows);
            }
        ],
        function (err, row_data) {
            if (dbc)
                db.release(dbc);

            if (err) {
                console.log("***** ERR / QUERY : " + JSON.stringify(err));
                callback(err);
            }
            else
                callback(null, row_data);
        });
};


exports.querySingle = function (queryString, values, callback) {
    var dbc;

    var queryTest = queryString;
    queryTest = queryTest.replace(/\?/g, "dieguen!hye");

    for (var i = 0; i < values.length; i++)
        queryTest = queryTest.replace("dieguen!hye", values[i]);

    console.log("***** query is : " + queryTest);

    async.waterfall([
            function (callback) {

                db.db(callback);
            },
            function (dbclient, callback) {

                dbc = dbclient;
                dbc.query(queryTest, callback);
            },
            function (rows, fields, callback) {

                if (!rows || rows.length == 0)
                    callback(helper.noSuchRow());
                else
                    callback(null, rows[0]);
            }
        ],
        function (err, rowData) {

            if (dbc)
                db.release(dbc);

            if (err) {
                console.log("***** ERR / QUERY : " + JSON.stringify(err));
                callback(err);
            }
            else
                callback(null, rowData);
        });
};


/*
 above db.query function calls sendFailure that sends a response message back to the client directly
 when no rows are returned for the select query.
 contrary, queryOk0 & querySingleOk0 return(callback, actually..) empty result set.
 these can be exploited in a case when it is necessary to find out whether there is a matching row in db table or not.
 */
exports.querySingleOk0 = function (queryString, values, callback) {

    var dbc;

    var queryTest = queryString;
    queryTest = queryTest.replace(/\?/g, "dieguen!hye");

    for (var i = 0; i < values.length; i++)
        queryTest = queryTest.replace("dieguen!hye", values[i]);

    console.log("***** query is : " + queryTest);

    async.waterfall([
            function (callback) {

                db.db(callback);
            },
            function (dbclient, callback) {

                dbc = dbclient;
                dbc.query(queryTest, callback);
            },
            function (rows, fields, callback) {

                if (!rows || rows.length == 0)
                    callback(null, {});
                else
                    callback(null, rows[0]);
            }
        ],
        function (err, rowData) {

            if (dbc)
                db.release(dbc);

            if (err) {
                console.log("***** ERR / QUERY : " + JSON.stringify(err));
                callback(err);
            }
            else
                callback(null, rowData);
        });
};

exports.queryOk0 = function (queryString, values, callback) {

    var dbc;

//    // replace '?' in the given query with values -> if uncomments this -> dbc.query(qs, cb). no values[]!
//    for (var i=0; i<values.length;i++)
//        queryString = queryString.replace("?", values[i]);

    // console.log( "query making... array is : " + values );
    // using '?' as identifier is not good since db query value may contain '?' as well.
    var queryTest = queryString;                                // so, replace '?' to 'dg!h' first.
    queryTest = queryTest.replace(/\?/g, "dieguen!hye");

    for (var i = 0; i < values.length; i++)                          //and then, replace 'dg!h' to db values
        queryTest = queryTest.replace("dieguen!hye", values[i]);

    console.log("***** query is : " + queryTest);


    async.waterfall([
            function (cb) {

                db.db(cb);
            },
            function (dbclient, cb) {

                dbc = dbclient;
                dbc.query(queryTest, cb);
            },
            function (rows, fields, cb) {
                if (!rows || rows.length == 0)
                    cb(null, []);   // return the empty set
                else
                    cb(null, rows);
            }
        ],
        function (err, rowData) {

            if (dbc)
                db.release(dbc);

            if (err) {
                console.log("***** ERR / QUERY : " + JSON.stringify(err));
                callback(err);
            }
            else
                callback(null, rowData);
        });
};

exports.escapeDQ = function (keyStr, valStr) {

    var jsonStr = valStr;

    // if (keyStr.length > 4)
    //   if (keyStr.substr(keyStr.length - 4) == "Json")     // if it ends with 'Json', Stringify this
    //     jsonStr = JSON.stringify(jsonStr);

    // console.log( "before jsonStr : " + jsonStr );

    jsonStr = mysql.escape(jsonStr);   // this will Parse ' ' ' and ' " '

    // console.log( "jsonStr : " + jsonStr );

    var finalStr = jsonStr.substr(1, jsonStr.length - 2);   // this will remove single quote (') pair.  'ted "taekyung" kwon' -> ted "taekyung" kwon
    return finalStr;
};

exports.multiQueryOk0 = function (queryStringArr, valuesArr, callback) {

    var dbc;

    var finalQueryArr = JSON.parse(JSON.stringify(queryStringArr));                                // so, replace '?' to 'dg!h' first.
    for (var i = 0; i < queryStringArr.length; i++) {
        // using '?' as identifier is not good since db query value may contain '?' as well.
        finalQueryArr[i] = finalQueryArr[i].replace(/\?/g, "dieguen!hye");
        for (var j = 0; j < valuesArr[i].length; j++)                          //and then, replace 'dg!h' to db values
            finalQueryArr[i] = finalQueryArr[i].replace("dieguen!hye", valuesArr[i][j]);
    }

    async.waterfall([
            // get a connection
            function (cb) {
                db.db(cb);
            },

            // sending query
            function (dbclient, cb) {
                dbc = dbclient;
                var rowArr = [], fieldArr = [];
                async.forEachOf(finalQueryArr, function (queryItem, idx, callback_each) {
                    // letting dbc.query parse&assemble introduced ER_PARSE_ERROR on DB server
                    console.log('your final query : ' + queryItem);
                    dbc.query(queryItem, function (err, result, field) {
                        if (err)
                            callback_each(err);
                        else {
                            rowArr[idx] = result;
                            fieldArr[idx] = field;
                            callback_each(null);
                        }
                    });
                }, function (err) {
                    if (err)
                        cb(err);
                    else
                        cb(null, rowArr, fieldArr);
                });
            }
        ],
        function (err, rowArr, fieldArr) {
            if (dbc)
                db.release(dbc);
            if (err) {
                callback(err);
            } else {
                callback(null, rowArr);
            }
        });
};


exports.getDBC = function (callback) {

    db.db(function (err, result) {

        if (err)
            callback(err);
        else
            callback(null, result);
    });
};

exports.endDBC = function (dbc) {

    if (dbc)
        db.release(dbc);
};

exports.queryOk0viaDBC = function (dbc, queryString, values, callback) {

    console.log("query making... array is : " + values);
    // using '?' as identifier is not good since db query value may contain '?' as well.
    var queryTest = queryString;                                // so, replace '?' to 'dg!h' first.
    queryTest = queryTest.replace(/\?/g, "dieguen!hye");

    for (var i = 0; i < values.length; i++)                          //and then, replace 'dg!h' to db values
        queryTest = queryTest.replace("dieguen!hye", values[i]);

    async.waterfall([
            // sending query
            function (cb) {
//                dbc.query(queryString, values, cb);
                dbc.query(queryTest, cb);           // letting dbc.query parse&assemble introduced ER_PARSE_ERROR on DB server

            },

            // retreive the result
            function (rows, fields, cb) {
                if (!rows || rows.length == 0)
                    cb(null, []);   // return the empty set
                else
                    cb(null, rows);
            }
        ],
        function (err, rowData) {
            if (dbc) db.release(dbc);
            if (err) {
                callback(err);
            } else {
//                console.log(rowData);
                callback(null, rowData);
            }
        });
};
