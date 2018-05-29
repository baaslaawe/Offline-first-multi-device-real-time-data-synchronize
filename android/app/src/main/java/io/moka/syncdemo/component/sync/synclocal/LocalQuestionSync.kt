package io.moka.syncdemo.component.sync.synclocal

import io.moka.syncdemo.component.sync.SyncTimeUtil
import io.moka.syncdemo.model.dao.question.QuestionDao
import io.moka.syncdemo.model.domain.Question
import io.moka.syncdemo.server.json.QuestionJson
import io.realm.Realm

object LocalQuestionSync {

    // Sync Day
    internal fun perform(realm: Realm, questionJsonList: List<QuestionJson>?) {
        if (null == questionJsonList)
            return

        for (i in questionJsonList.indices) {

            val questionJson = questionJsonList[i]
            val serverId = questionJson.server_id

            val syncedLocalQuestion = realm.where(Question::class.java).equalTo("serverId", serverId).findFirst()

            if (null != syncedLocalQuestion) {
                // 동기화 된적이 있다.

                if (syncedLocalQuestion.dirtyFlag) {
                    // 수정이 된적이 있는가 ?? 충돌인가 ?? 검사 updated_at 으로 최신것 검사

                    val localSyncedUpdatedAt = SyncTimeUtil.syncUpdatedAt(syncedLocalQuestion.updatedAt)
                    if (localSyncedUpdatedAt < questionJson.updated_at ?: 0) {
                        // 서버의것 선택 -> update

                        QuestionDao.update(domain = null,
                                realm = realm,
                                id = syncedLocalQuestion.id,
                                update = {
                                    it.name = questionJson.name ?: ""
                                    it.say = questionJson.say ?: ""
                                    it.imageUrl = questionJson.imageUrl ?: ""
                                    it.question = questionJson.question ?: ""
                                }, syncFlag = true)
                    }
                }
                else { // 충돌이 아니다. 바로 update
                    QuestionDao.update(domain = null,
                            realm = realm,
                            id = syncedLocalQuestion.id,
                            update = {
                                it.name = questionJson.name ?: ""
                                it.say = questionJson.say ?: ""
                                it.imageUrl = questionJson.imageUrl ?: ""
                                it.question = questionJson.question ?: ""
                            }, syncFlag = true)
                }
            }
            else { // insert

                QuestionDao.insert(
                        realm = realm,
                        insert = { day ->
                            day.serverId = questionJson.server_id ?: 0

                            day.name = questionJson.name
                            day.say = questionJson.say
                            day.imageUrl = questionJson.imageUrl
                            day.question = questionJson.question
                        }, syncFlag = true)
            }
        }
    }

}