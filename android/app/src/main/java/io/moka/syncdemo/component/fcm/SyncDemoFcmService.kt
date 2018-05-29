package io.moka.syncdemo.component.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.moka.syncdemo.component.sync.SyncAdapter

class SyncDemoFcmService : FirebaseMessagingService() {

    companion object {
        val KEY_TYPE = "type"
        val KEY_MESSAGE = "message"
    }

    /* 포그라운드 일때만 이쪽으로 들어온다 */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.wtf("SyncDemoFcmService", "From : " + remoteMessage.from)

        if (remoteMessage.data.isNotEmpty()) {
            Log.wtf("SyncDemoFcmService", "Message data payload: " + remoteMessage.data)

            SyncAdapter.performSync()
        }
    }

}