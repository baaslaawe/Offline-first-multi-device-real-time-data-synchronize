var async = require('async');

var db = require("./../util/db");
var helper = require("../util/helpers.js");
var Util = require("../util/util.js");

/** DB Schema Answer Table ***********************************************************************

 `id` int not null auto_increment,
 `local_id` int not null default 0,
 `question_id` int not null default 0,

 `name` varchar(1024) default null,
 `answer` varchar(1024) default null,

 `created_at` int DEFAULT 0,
 `updated_at` int DEFAULT 0

 */

/** Class ******************************************************************************************
 */

function Answer (answerData) {

    this.id = answerData["id"];
    this.local_id = answerData["local_id"];
    this.question_id = answerData["question_id"];

    this.name = answerData["name"];
    this.answer = answerData["answer"];

    if (answerData["created_at"])
        this.created_at = answerData["created_at"];
    if (answerData["updated_at"])
        this.updated_at = answerData["updated_at"];
}

Answer.prototype.created_at = Util.current_timestamp();
Answer.prototype.updated_at = Util.current_timestamp();

Answer.prototype.insert = function (callback) {
    if (!validateInsert()) {
        callback(helper.invalidError("inValid"));
        return;
    }

    this.created_at = Util.current_timestamp();
    this.updated_at = Util.current_timestamp();

    var columnField = "";
    var valueField = "";

    for (var key in this) {

        var quote = "";
        var value = this[key];

        if (typeof value != 'function' && value != undefined) {

            if (typeof value === 'string' || typeof value === 'object') {

                quote = '"';
                value = db.escapeDQ(key, value);
            }

            columnField = columnField + "," + key;
            valueField = valueField + "," + quote + value + quote;
        }
    }

    columnField = columnField.substr(1);
    valueField = valueField.substr(1);

    db.query("INSERT INTO answer (?) VALUES (?);"
        , [columnField, valueField]
        , callback);
};

var validateInsert = function () {
    return true;
};

Answer.prototype.update = function (callback) {
    if (!validateUpdate(this, callback))
        return;

    var answer = Object.assign({}, this);
    var id = answer.id;
    delete answer.id;

    answer.created_at = undefined;
    answer.updated_at = Util.current_timestamp();

    var queryColVal = "";

    for (var key in this) {

        var quote = "";
        var value = answer[key];

        if (typeof value != 'function' && value != undefined) {

            if (typeof value === 'string' || typeof value === 'object') {   // if it needs to be escaped?

                quote = '"';
                value = db.escapeDQ(key, value);
            }
            queryColVal += ", " + key + "= " + quote + value + quote;
        }
    }
    queryColVal = queryColVal.substr(1);

    if (queryColVal.length == 0)
        callback(null);
    else
        db.query("UPDATE answer SET ? WHERE id = ?"
            , [queryColVal, id]
            , callback);
};

function validateUpdate (object, callback) {
    if (!object.id) {
        callback(helper.missingData("id"));
        return false;
    }
    return true;
}

/** Method *****************************************************************************************
 */


/** Function ***************************************************************************************
 */

exports.new = function (answer) {
    return new Answer(answer);
};

exports.find = function (field, value, callback) {
    db.query("SELECT * FROM answer WHERE answer." + field + " = '?'"
        , [value]
        , callback);
};

exports.findById = function (id, callback) {
    db.querySingle("SELECT * FROM answer WHERE id = ?"
        , [id]
        , callback);
};

exports.deleteById = function (id, callback) {
    db.query("DELETE FROM answer WHERE id = ?"
        , [id]
        , callback);
};

exports.getListForSync = function (syncAt, callback) {
    db.queryOk0(
        "SELECT * FROM answer " +
        "WHERE 1 AND updated_at > " + syncAt + " ORDER BY created_at DESC"
        , []
        , callback);
};

/**
 */

var makeOrderWhere = function (params) {
    var userWhere = (params.user_id) ? " AND user_id=" + params.user_id : '';
    return ' AND archived = 0' + userWhere
};

exports.countAnswer = function (params, callback) {
    var whereQuery = makeOrderWhere(params);

    db.query(
        "( SELECT count(id) AS count FROM answer WHERE 1 " + whereQuery + " )"
        , []
        , callback);
};

exports.getAnswerList = function (params, callback) {
    var limitWhere = (params.limitTo) ? " LIMIT " + params.limitTo : "";
    var pageWhere = (params.lastId) ? " AND answer.id<" + params.lastId : "";

    var whereQuery = makeOrderWhere(params);

    db.queryOk0(
        "SELECT id, answer.question_id, name, answer, created_at, updated_at FROM answer" +
        " WHERE 1 " + pageWhere + whereQuery +
        " ORDER BY answer.id DESC" + limitWhere
        , []
        , callback)
};

exports.getQuestionId = function (question_local_id, callback) {
    db.querySingleOk0(
        "SELECT id FROM question " +
        "WHERE local_id=?"
        , [question_local_id]
        , callback);
};