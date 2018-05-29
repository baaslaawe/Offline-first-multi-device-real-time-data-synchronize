package io.moka.syncdemo.server.json


import com.google.gson.annotations.SerializedName
import io.moka.syncdemo.model.domain.Answer


class AnswerJson {

    @SerializedName("id")
    var server_id: Long? = null
    var local_id: Long? = null
    @SerializedName("question_id")
    var question_server_id: Long? = null
    var question_local_id: Long? = null

    var name: String? = null
    var answer: String? = null

    var created_at: Long? = null
    var updated_at: Long? = null

    companion object {

        fun newAnswerJson(answer: Answer): AnswerJson {
            val answerJson = AnswerJson()
            answerJson.server_id = answer.serverId
            answerJson.local_id = answer.id
            answerJson.question_server_id = answer.questionServerId
            answerJson.question_local_id = answer.questionId

            answerJson.name = answer.name
            answerJson.answer = answer.answer

            return answerJson
        }
    }

}
