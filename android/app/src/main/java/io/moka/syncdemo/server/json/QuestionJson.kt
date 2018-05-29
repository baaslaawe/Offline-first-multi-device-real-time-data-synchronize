package io.moka.syncdemo.server.json


import com.google.gson.annotations.SerializedName
import io.moka.syncdemo.model.domain.Question


class QuestionJson {

    @SerializedName("id")
    var server_id: Long? = null
    var local_id: Long? = null

    var name: String? = null
    var say: String? = null
    var imageUrl: String? = null
    var question: String? = null

    var created_at: Long? = null
    var updated_at: Long? = null

    companion object {

        fun newQuestionJson(question: Question): QuestionJson {
            val dayJson = QuestionJson()
            dayJson.server_id = question.serverId
            dayJson.local_id = question.id
            dayJson.name = question.name
            dayJson.say = question.say
            dayJson.imageUrl = question.imageUrl
            dayJson.question = question.question

            return dayJson
        }
    }

}
