var async = require('async');

var db = require("./../util/db");
var helper = require("../util/helpers.js");
var Util = require("../util/util.js");

/** DB Schema Question Table ***********************************************************************

 `id` int not null auto_increment,
 `local_id` int not null default 0,

 `name` varchar(1024) default null,
 `say` varchar(1024) default null,
 `image_url` varchar(1024) default null,
 `question` varchar(1024) default null,

 `created_at` int DEFAULT 0,
 `updated_at` int DEFAULT 0

 */

/** Class ******************************************************************************************
 */

function Question (questionData) {

    this.id = questionData["id"];
    this.local_id = questionData["local_id"];

    this.name = questionData["name"];
    this.say = questionData["say"];
    this.image_url = questionData["image_url"];
    this.question = questionData["question"];

    if (questionData["created_at"])
        this.created_at = questionData["created_at"];
    if (questionData["updated_at"])
        this.updated_at = questionData["updated_at"];
}

Question.prototype.created_at = Util.current_timestamp();
Question.prototype.updated_at = Util.current_timestamp();

Question.prototype.insert = function (callback) {
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

    db.query("INSERT INTO question (?) VALUES (?);"
        , [columnField, valueField]
        , callback);
};

var validateInsert = function () {
    return true;
};

Question.prototype.update = function (callback) {
    if (!validateUpdate(this, callback))
        return;

    var question = Object.assign({}, this);
    var id = question.id;
    delete question.id;

    question.created_at = undefined;
    question.updated_at = Util.current_timestamp();

    var queryColVal = "";

    for (var key in this) {

        var quote = "";
        var value = question[key];

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
        db.query("UPDATE question SET ? WHERE id = ?"
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

exports.new = function (question) {
    return new Question(question);
};

exports.find = function (field, value, callback) {
    db.query("SELECT * FROM question WHERE question." + field + " = '?'"
        , [value]
        , callback);
};

exports.findById = function (id, callback) {
    db.querySingle("SELECT * FROM question WHERE id = ?"
        , [id]
        , callback);
};

exports.deleteById = function (id, callback) {
    db.query("DELETE FROM question WHERE id = ?"
        , [id]
        , callback);
};

exports.getListForSync = function (syncAt, callback) {
    db.queryOk0(
        "SELECT * FROM question " +
        "WHERE 1 AND updated_at > " + syncAt + " ORDER BY created_at DESC"
        , []
        , callback);
};

/**
 */

exports.getAllWithAnswer = function (callback) {
    db.queryOk0(
        "SELECT id, name as q_name, question as q_question, say as q_say, image_url as q_image_url, A.a_name, A.answer, created_at, updated_at FROM question " +
        "LEFT JOIN (SELECT id as a_id, question_id, name as a_name, answer FROM answer) as A ON id = A.question_id ORDER BY id DESC"
        , []
        , callback)
};
