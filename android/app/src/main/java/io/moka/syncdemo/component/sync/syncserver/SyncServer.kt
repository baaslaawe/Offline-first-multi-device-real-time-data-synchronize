package io.moka.syncdemo.component.sync.syncserver


import io.moka.syncdemo.model.domain.Answer
import io.moka.syncdemo.model.domain.Question
import io.moka.syncdemo.server.json.AnswerJson
import io.moka.syncdemo.server.json.QuestionJson
import io.moka.syncdemo.server.request.SyncServerReq
import io.realm.Realm
import io.realm.RealmResults


class SyncServer {

    private var realm: Realm? = null

    val syncServerBody: SyncServerReq
        get() {
            val syncServerRequestBody = SyncServerReq()

            val dirtyQuestions = realm!!.where(Question::class.java).equalTo("dirtyFlag", true).findAll()
            makeDirtyQuestions(dirtyQuestions, syncServerRequestBody)

            val dirtyAnswers = realm!!.where(Answer::class.java).equalTo("dirtyFlag", true).findAll()
            makeDirtyAnswers(dirtyAnswers, syncServerRequestBody)

            return syncServerRequestBody
        }

    /**
     */

    internal fun makeDirtyQuestions(dirtyQuestions: RealmResults<Question>, syncServerReq: SyncServerReq) {
        for (question in dirtyQuestions) {
            val dayJson = QuestionJson.newQuestionJson(question)

            if (0L != question.serverId)
                syncServerReq.patch.questions.add(dayJson)
            else
                syncServerReq.post.questions.add(dayJson)
        }
    }

    internal fun makeDirtyAnswers(dirtyAnswers: RealmResults<Answer>, syncServerReq: SyncServerReq) {
        for (answer in dirtyAnswers) {
            val dayJson = AnswerJson.newAnswerJson(answer)

            if (0L != answer.serverId)
                syncServerReq.patch.answers.add(dayJson)
            else
                syncServerReq.post.answers.add(dayJson)
        }
    }

    /**
     */

    fun setRealmInstance(realmInstance: Realm): SyncServer {
        this.realm = realmInstance
        return this
    }

    companion object {

        fun newInstance(): SyncServer {
            return SyncServer()
        }
    }

}
