package io.moka.syncdemo.component.sync.syncserver


import android.content.Context
import io.moka.syncdemo.model.domain.Answer
import io.moka.syncdemo.model.domain.Question
import io.moka.syncdemo.server.response.SyncServerRes
import io.realm.Realm


class ResponseSyncServer(private val context: Context) {

    private var realm: Realm? = null

    fun processBy(response: SyncServerRes?) {
        if (null == response)
            return

        realm!!.beginTransaction()

        /* question */
        setPostQuestionIds(response.data.result?.postQuestion)
        setPatchQuestionIds(response.data.result?.patchQuestion)

        /* answer */
        setPostAnswerIds(response.data.result?.postAnswer)
        setPatchAnswerIds(response.data.result?.patchAnswer)

        realm!!.commitTransaction()
    }

    /**
     * About Question
     */

    private fun setPostQuestionIds(postQuestionResList: List<SyncServerRes.PostRes>?) {
        if (null == postQuestionResList)
            return

        for (postQuestionRes in postQuestionResList) {
            val question = realm!!.where(Question::class.java).equalTo("id", postQuestionRes.local_id).findFirst()

            question?.serverId = postQuestionRes.server_id
            question?.dirtyFlag = false
        }
    }

    private fun setPatchQuestionIds(patchResList: List<SyncServerRes.PatchRes>?) {
        if (null == patchResList)
            return

        for (patchRes in patchResList) {
            val question = realm!!.where(Question::class.java).equalTo("id", patchRes.local_id).findFirst()

            question?.dirtyFlag = false
        }
    }

    /**
     * About Answer
     */

    private fun setPostAnswerIds(postAnswerResList: List<SyncServerRes.PostRes>?) {
        if (null == postAnswerResList)
            return

        for (postAnswerRes in postAnswerResList) {
            val answer = realm!!.where(Answer::class.java).equalTo("id", postAnswerRes.local_id).findFirst()

            answer?.serverId = postAnswerRes.server_id
            answer?.dirtyFlag = false
        }
    }

    private fun setPatchAnswerIds(patchResList: List<SyncServerRes.PatchRes>?) {
        if (null == patchResList)
            return

        for (patchRes in patchResList) {
            val question = realm!!.where(Question::class.java).equalTo("id", patchRes.local_id).findFirst()

            question?.dirtyFlag = false
        }
    }

    /**
     */

    fun setRealmInstance(realmInstance: Realm): ResponseSyncServer {
        this.realm = realmInstance
        return this
    }

    companion object {

        fun from(context: Context): ResponseSyncServer {
            return ResponseSyncServer(context)
        }
    }

}
