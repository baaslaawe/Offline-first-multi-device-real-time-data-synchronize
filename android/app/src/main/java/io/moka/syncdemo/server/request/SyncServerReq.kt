package io.moka.syncdemo.server.request

import io.moka.syncdemo.server.json.AnswerJson
import io.moka.syncdemo.server.json.QuestionJson
import java.util.ArrayList


class SyncServerReq {

    var post = Post()
    var patch = Patch()

    class Post {
        var questions: ArrayList<QuestionJson> = ArrayList()
        var answers: ArrayList<AnswerJson> = ArrayList()
    }

    class Patch {
        var questions: ArrayList<QuestionJson> = ArrayList()
        var answers: ArrayList<AnswerJson> = ArrayList()
    }

}
