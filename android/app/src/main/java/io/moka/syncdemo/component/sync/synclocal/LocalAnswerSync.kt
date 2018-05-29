package io.moka.syncdemo.component.sync.synclocal

import io.moka.syncdemo.component.sync.SyncTimeUtil
import io.moka.syncdemo.model.dao.answer.AnswerDao
import io.moka.syncdemo.model.domain.Answer
import io.moka.syncdemo.model.domain.Question
import io.moka.syncdemo.server.json.AnswerJson
import io.realm.Realm

object LocalAnswerSync {

    // Sync Day
    internal fun perform(realm: Realm, answerJsonList: List<AnswerJson>?) {
        if (null == answerJsonList)
            return

        for (i in answerJsonList.indices) {

            val answerJson = answerJsonList[i]
            val serverId = answerJson.server_id
            val questionServerId = answerJson.question_server_id

            val question = realm.where(Question::class.java).equalTo("serverId", questionServerId).findFirst()

            val syncedLocalAnswer = realm.where(Answer::class.java).equalTo("serverId", serverId).findFirst()

            if (null != syncedLocalAnswer) {
                // 동기화 된적이 있다.

                if (syncedLocalAnswer.dirtyFlag) {
                    // 수정이 된적이 있는가 ?? 충돌인가 ?? 검사 updated_at 으로 최신것 검사

                    val localSyncedUpdatedAt = SyncTimeUtil.syncUpdatedAt(syncedLocalAnswer.updatedAt)
                    if (localSyncedUpdatedAt < answerJson.updated_at ?: 0) {
                        // 서버의것 선택 -> update

                        AnswerDao.update(domain = null,
                                realm = realm,
                                id = syncedLocalAnswer.id,
                                update = {
                                    it.questionId = question?.id ?: 0
                                    it.questionServerId = answerJson.question_server_id ?: 0

                                    it.name = answerJson.name ?: ""
                                    it.answer = answerJson.answer ?: ""
                                }, syncFlag = true)
                    }
                }
                else { // 충돌이 아니다. 바로 update
                    AnswerDao.update(domain = null,
                            realm = realm,
                            id = syncedLocalAnswer.id,
                            update = {
                                it.questionId = question?.id ?: 0
                                it.questionServerId = answerJson.question_server_id ?: 0

                                it.name = answerJson.name ?: ""
                                it.answer = answerJson.answer ?: ""
                            }, syncFlag = true)
                }
            }
            else { // insert

                AnswerDao.insert(
                        realm = realm,
                        insert = { answer ->
                            answer.serverId = answerJson.server_id ?: 0
                            answer.questionId = question?.id ?: 0
                            answer.questionServerId = answerJson.question_server_id ?: 0

                            answer.name = answerJson.name
                            answer.answer = answerJson.answer
                        }, syncFlag = true)
            }
        }
    }

}