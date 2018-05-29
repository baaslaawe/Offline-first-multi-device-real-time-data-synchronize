package io.moka.syncdemo.component.sync.synclocal

import io.moka.syncdemo.server.response.SyncLocalRes
import io.realm.Realm


class SyncLocal {

    private lateinit var realm: Realm
    private lateinit var response: SyncLocalRes
    private var uid: Long = 0

    fun processSync() {
        LocalQuestionSync.perform(realm, response.data.result?.questions)
        LocalAnswerSync.perform(realm, response.data.result?.answers)
    }

    /**
     */

    fun setRealmInstance(realmInstance: Realm): SyncLocal {
        this.realm = realmInstance
        return this
    }

    fun setResponse(response: SyncLocalRes): SyncLocal {
        this.response = response
        return this
    }

    fun setUserId(uid: Long): SyncLocal {
        this.uid = uid
        return this
    }

}
