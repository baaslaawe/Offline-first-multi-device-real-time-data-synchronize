var url = require('url');
var async = require('async');
var moment = require('moment');

var helpers = require('./../util/helpers.js');
var Util = require('./../util/util.js');

var Answer = require('../model/Answer.js');
var Question = require('../model/Question.js');

exports.syncLocal = function (req, res) {
    var params = url.parse(req.url, true).query;
    var user = req.user;

    if (!params.syncAt)
        helpers.sendFailure(req, res, helpers.missingData("syncAt"));
    else {

        // 각 테이블에서 syncAt 이후로 업데이트 된 정보를 가져온다
        async.parallel({
                questions: (callback) => {
                    Question.getListForSync(params.syncAt, (err, results) => {
                        err ? callback(err) : callback(null, results);
                    });
                },
                answers: (callback) => {
                    Answer.getListForSync(params.syncAt, (err, results) => {
                        err ? callback(err) : callback(null, results);
                    });
                }
            },
            (err, results) => {
                if (err)
                    helpers.sendFailure(req, res, err);
                else
                    helpers.sendSuccess(req, res, {
                        result: results,
                        syncAt: Util.current_timestamp()
                    });
            })
    }
};

exports.syncServer = function (req, res) {
    var postData = req.body;

    if (!validateBody(req, res, postData))
        return;

    /**
     * 로컬에서 dirtyFlag 가 true 인 데이터의 Post 또는 Patch 데이터들 받아서 서버에서 동기화 처리
     * @post{ List<> list }, @patch{ List<> list }
     *
     * ------------------------------------
     * ### Post
     * - 상위 모델 id 를 가지지 않을 경우
     * post 로 들어온 데이터를 그냥 다 insert 시킨다
     *
     * - 상위 모델 id 를 가져 의존성이 있을경우
     * server_상위_id 가 있으면 바로 insert 시키고, 없을경우 local_상위_id 를 이용해 상위테이블에서 찾아 id 값을 찾아온다
     *
     * ------------------------------------
     * ### Patch
     * (이미 한번 동기화 되어 server_id 를 로컬에서 가지고있다.)
     * - 해당 id 를 가진 데이터를 update 시킨다
     */
    async.parallel({
            /**
             * Question
             */
            postQuestion: function (callback) {
                var questionJsonList = postData.post.questions;

                if (!questionJsonList || questionJsonList.length == 0) {
                    callback(null);
                    return;
                }

                var questionIds = [];
                async.eachSeries(questionJsonList,
                    function (questionJson, callback_eachQuestion) {

                        var newQuestion = Question.new(questionJson);
                        newQuestion.insert(function (err, result) {

                            if (err)
                                callback_eachQuestion(err);
                            else {

                                questionIds.push({
                                    local_id: questionJson.local_id,
                                    server_id: result.insertId
                                });
                                callback_eachQuestion(null);
                            }
                        });
                    },
                    function (err) {
                        err ? callback(err) : callback(null, questionIds);
                    });
            },
            patchQuestion: function (callback) {
                var questionJsonList = postData.patch.questions;

                if (!questionJsonList || questionJsonList.length == 0) {
                    callback(null);
                    return;
                }

                var questionIds = [];
                async.each(questionJsonList,
                    function (questionJson, callback_eachQuestion) {

                        var newQuestion = Question.new(questionJson);
                        newQuestion.update(function (err, result) {

                            if (err)
                                callback_eachQuestion(err);
                            else {

                                questionIds.push({
                                    local_id: questionJson.local_id
                                });
                                callback_eachQuestion(null);
                            }
                        });
                    },
                    function (err) {
                        err ? callback(err) : callback(null, questionIds);
                    });
            },

            /**
             * Answer
             * 상위 모델로 Question 이 있고, 필수 이다.
             */
            postAnswer: function (callback) {
                var answerJsonList = postData.post.answers;

                if (!answerJsonList || answerJsonList.length == 0) {
                    callback(null);
                    return;
                }

                var answerIds = [];
                async.eachSeries(answerJsonList,
                    function (answerJson, callback_eachAnswer) {

                        /*
                         상위모델 question_server_id
                         __ 없으면 -> spent_type_local_id 를 가지고 spentType 테이블에서 찾아 id 값을 가져와서 넣는다.
                         __ 있으면 -> 그대로 insert

                         찾아서 없으면 그냥 0 이나 null 로 넣는다. (수익)
                         */

                        if (!answerJson.question_id || 0 == answerJson.question_id) {

                            /* 상위 SpentType 의 Id 를 가지고 온다 */
                            Answer.getQuestionId(answerJson.question_local_id, (err, result) => {
                                if (!err && result.length > 0)
                                    answerJson.question_id = result.id;

                                var newAnswer = Answer.new(answerJson);
                                newAnswer.insert(function (err, result) {

                                    if (err)
                                        callback_eachAnswer(err);
                                    else {
                                        answerIds.push({
                                            local_id: answerJson.local_id,
                                            server_id: result.insertId
                                        });
                                        callback_eachAnswer(null);
                                    }
                                });
                            });
                        }
                        else {
                            var newAnswer = Answer.new(answerJson);
                            newAnswer.insert(function (err, result) {

                                if (err)
                                    callback_eachAnswer(err);
                                else {
                                    answerIds.push({
                                        local_id: answerJson.local_id,
                                        server_id: result.insertId
                                    });
                                    callback_eachAnswer(null);
                                }
                            });
                        }
                    },
                    function (err) {
                        err ? callback(err) : callback(null, answerIds);
                    });
            },
            patchAnswer: function (callback) {
                var answerList = postData.patch.answers;

                if (!answerList || answerList.length == 0) {
                    callback(null);
                    return;
                }

                var answerIds = [];
                async.each(answerList,
                    function (answerJson, callback_eachSpentType) {

                        var newAnswer = Answer.new(answerJson);
                        newAnswer.update(function (err, result) {

                            if (err)
                                callback_eachSpentType(err);
                            else {
                                answerIds.push({
                                    local_id: answerJson.local_id
                                });
                                callback_eachSpentType(null);
                            }
                        });
                    },
                    function (err) {
                        err ? callback(err) : callback(null, answerIds);
                    });
            }
        },
        function (err, results) {

            if (err)
                helpers.sendFailure(req, res, err);
            else {
                if (results.postQuestion || results.patchQuestion || results.postAnswer || results.patchAnswer) {
                    pushFcm();
                }

                helpers.sendSuccess(req, res, {
                    result: results,
                    syncAt: Util.current_timestamp()
                });
            }
        })
};

var validateBody = function (req, res, postData) {
    if (!postData.post) {
        helpers.sendFailure(req, res, helpers.missingData("post"));
        return false;
    }
    if (!postData.patch) {
        helpers.sendFailure(req, res, helpers.missingData("patch"));
        return false;
    }

    return true;
};

/**
 */

var pushFcm = function (callback) {
    var FCM = require('fcm-node');
    var serverKey = 'AIzaSyBY96VIWF_lDERCZ8CksEf4aGXd_00Hym8'; //put your server key here
    var fcm = new FCM(serverKey);

    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
        to: '/topics/general',
        collapse_key: 'your_collapse_key',

        data: {  //you can send only notification or only data(or include both)
            sync: 'true',
        }
    };

    fcm.send(message, function (err, response) {
        if (callback)
            callback();

        if (err) {
            console.log("##\nSomething has gone wrong!\n##");
        } else {
            console.log("##\nSuccessfully sent with response: " + response + "\n##");
        }
    });
};

/**
 */

/*
 id: 8,
 q_name: "moka",
 q_question: "test test",
 q_say: "Hi, I'm developer MOKA. Nice to meet you",
 q_image_url: null,
 a_name: "은후",
 answer: "답변입니다",
 created_at: 1527534950,
 updated_at: 1527534950
 */
exports.getAll = function (req, res) {
    Question.getAllWithAnswer((err, results) => {
        if (err)
            helpers.sendFailure(req, res, err);
        else {
            var questionWithAnswers = [];

            var tempBefore;
            results.forEach((result) => {
                if (tempBefore && result.id == tempBefore.id) {
                    var questionWithAnswer = tempBefore;
                    var answers = questionWithAnswer.answers;
                    answers.push({
                        answer: result.answer,
                        a_name: result.a_name
                    });

                    questionWithAnswer.answers = answers;
                    tempBefore = questionWithAnswer;
                }
                else {
                    var questionWithAnswer = result;

                    if (result.answer) {
                        var answer = {
                            answer: result.answer,
                            a_name: result.a_name
                        };
                        questionWithAnswer.answers = [answer];
                    }
                    else {
                        questionWithAnswer.answers = [];
                    }

                    delete questionWithAnswer.answer;
                    delete questionWithAnswer.a_name;

                    questionWithAnswers.push(questionWithAnswer);
                    tempBefore = questionWithAnswer;
                }
            });

            helpers.sendSuccess(req, res, {
                result: questionWithAnswers
            });
        }
    })
};

exports.insertAnswer = function (req, res) {

};