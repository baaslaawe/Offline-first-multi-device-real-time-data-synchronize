var path = require('path');

exports.version = '0.1.0';

exports.sendSuccess = function (req, res, data) {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.writeHead(200, { "Content-Type": "application/json;charset=UTF-8" });
    var output = { error: null, data: data };
    res.end(JSON.stringify(output) + "\n");
};

exports.sendFailure = function (req, res, err) {
    var code = (err.code) ? err.code : err.name;
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.writeHead(400, { "Content-Type": "application/json;charset=UTF-8" });      // 400 -> bad request
    var output = { error: code, message: err.message };
    res.end(JSON.stringify(output) + "\n");
};

exports.sendFailureWithMessage = function (req, res, message) {
    var err = exports.invalidError(message);
    var code = (err.code) ? err.code : err.name;
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.writeHead(400, { "Content-Type": "application/json;charset=UTF-8" });      // 400 -> bad request
    var output = { error: code, message: err.message };
    res.end(JSON.stringify(output) + "\n");
};

exports.verify = function (data, field_names) {
    for (var i = 0; i < field_names.length; i++) {
        if (!data[field_names[i]]) {
            throw exports.error("missingData",
                field_names[i] + " not optional");
        }
    }
    return true;
};

exports.error = function (code, message) {
    var e = new Error(message);
    e.code = code;
    return e;
};

exports.invalidError = function (message) {
    return exports.error("invalidError",
        message);
};

exports.invalidResource = function () {
    return exports.error("invalidResource",
        "The requested resource does not exist.");
};

exports.fileError = function (err) {
    return exports.error("fileError", JSON.stringify(err.message));
};

exports.validFilename = function (fn) {
    var re = /[^\.a-zA-Z0-9_-]/;
    return typeof fn == 'string' && fn.length > 0 && !(fn.match(re));
};

exports.dbError = function () {
    return exports.error("dbError",
        "Something horrible has happened with our database!");
};

exports.missingData = function (field) {
    return exports.error("missingData", "You must provide: " + field);
};

exports.noSuchUser = function () {
    return exports.error("noSuchUser",
        "The specified user does not exist");
};

exports.noSuchUserStat = function () {
    return exports.error("noSuchUserStat",
        "The specified userstat does not exist");
};

exports.noSuchImage = function () {
    return exports.error("noSuchImage",
        "The specified image does not exist");
};

exports.noSuchVideo = function () {
    return exports.error("noSuchVideo",
        "The specified video does not exist");
};

exports.noSuchRow = function () {
    return exports.error("noSuchRow",
        "The specified data does not exist in db");
};

exports.userAlreadyRegistered = function () {
    return exports.error("userAlreadyRegistered",
        "This user appears to exist already!");
};

exports.noAuthority = function () {
    return exports.error("noAuthority",
        "This user has no authority to the request!");
};