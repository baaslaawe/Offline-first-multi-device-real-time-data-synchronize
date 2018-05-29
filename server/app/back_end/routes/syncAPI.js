/*
 */

var sync_controller = require('../controller/sync_controller.js');
var common_controller = require('../controller/common_controller.js');

module.exports = function (app) {

    /* Sync */
    app.get('/v1/api/syncLocal', sync_controller.syncLocal);
    app.post('/v1/api/syncServer', sync_controller.syncServer);

    /* For web client */
    app.get('/v1/api/qna', sync_controller.getAll);
    app.post('/v1/api/answer', sync_controller.insertAnswer);

};
