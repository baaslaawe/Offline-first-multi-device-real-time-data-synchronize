package io.moka.syncdemo.component.sync

import android.app.IntentService
import android.content.Intent
import io.moka.mokabaselib.util.log.MLog
import io.moka.syncdemo.component.eventbus.Events
import io.moka.syncdemo.component.eventbus.RxBus
import io.moka.syncdemo.component.preference.SDPref
import io.moka.syncdemo.component.sync.synclocal.SyncLocal
import io.moka.syncdemo.component.sync.syncserver.ResponseSyncServer
import io.moka.syncdemo.component.sync.syncserver.SyncServer
import io.moka.syncdemo.model.RealmHelper
import io.moka.syncdemo.server.api.AppAPI
import io.moka.syncdemo.util.DateUtil
import io.realm.Realm


class SyncService : IntentService("SyncService") {

    lateinit var api: AppAPI.API

    private lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()
        MLog.deb("________ SyncService's onCreate is Called ________")
        api = AppAPI.api
    }

    override fun onHandleIntent(intent: Intent?) {
        MLog.deb(":: SyncService is starting ... ::")
        realm = RealmHelper.instance
        val onlySyncLocal = intent!!.getBooleanExtra(KEY_ONLY_SYN_LOCAL, false)

        if (syncLocal() && !onlySyncLocal)
            syncServer()

        realm.close()

        RxBus.publish(Events.REFRESH_FOR_SYNC, true)
        SyncAdapter.finishSync()
        MLog.deb(":: Finally SyncService is end ::")
    }

    /**
     * SyncLocal
     * server data to local data synchronize
     */
    private fun syncLocal(): Boolean {
        try {
            MLog.deb(":: SyncLocal is Start ::")
            /* Call Api */
            val response = api.syncLocal(SDPref.syncedAt).execute()

            if (response.isSuccessful) {
                SyncTimeUtil.setTimeGap(DateUtil.timestampInSecond, response.body()!!.data.syncAt)

                SyncLocal()
                        .setRealmInstance(realm)
                        .setResponse(response.body()!!)
                        .processSync()

                realm.beginTransaction()
                SDPref.syncedAt = response.body()!!.data.syncAt
                realm.commitTransaction()

                MLog.deb(":: SyncLocal is End ::")
                return true
            }
            else {
                MLog.deb(":: SyncLocal is Error :: ${response.errorBody()?.string()}")
                return false
            }
        } catch (ignore: Exception) {
            MLog.deb(":: SyncLocal is err ::")
            ignore.printStackTrace()
            return false
        }
    }

    /**
     * SyncServer
     * local data to server data synchronize
     */
    private fun syncServer() {
        try {
            MLog.deb(":: SyncServer is Start ::")
            val syncServerRequestBody =
                    SyncServer
                            .newInstance()
                            .setRealmInstance(realm)
                            .syncServerBody

            /* Call Api */
            val response = api.syncServer(syncServerRequestBody).execute()

            if (response.isSuccessful) {
                ResponseSyncServer
                        .from(this)
                        .setRealmInstance(realm)
                        .processBy(response.body())

                realm.beginTransaction()
                SDPref.syncedAt = response.body()!!.data.syncAt
                realm.commitTransaction()

                MLog.deb(":: SyncServer is End ::")
            }
            else {
                MLog.deb(":: SyncLocal is Error :: ${response.errorBody()?.string()}")
            }
        } catch (ignore: Exception) {
            MLog.deb(":: SyncServer is err")
            ignore.printStackTrace()
        }
    }

    companion object {
        val KEY_ONLY_SYN_LOCAL = "SyncService.KEY_ONLY_SYN_LOCAL"
    }

}
