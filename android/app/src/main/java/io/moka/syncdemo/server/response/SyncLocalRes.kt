package io.moka.syncdemo.server.response

import io.moka.syncdemo.server.json.AnswerJson
import io.moka.syncdemo.server.json.QuestionJson


class SyncLocalRes : BaseRes() {

    var error: String? = null
    lateinit var data: Data

    class Data {

        var result: Result? = null
        var syncAt: Long = 0

    }

    class Result {
        var questions: List<QuestionJson>? = null
        var answers: List<AnswerJson>? = null
    }

}
