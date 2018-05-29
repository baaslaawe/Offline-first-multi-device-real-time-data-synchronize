package io.moka.syncdemo.component.sync

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import io.moka.mokabaselib.util.log.MLog
import io.moka.syncdemo._Application
import io.moka.syncdemo.util.postDelay

@SuppressLint("StaticFieldLeak")
object SyncAdapter {

    private var context: Context = _Application.context
    private var isSyncing: Boolean = false

    /**
     */

    fun performSync(force: Boolean = false, onlySyncLocal: Boolean = false) {
        /* debug 모드일때 동기화 요청 하지 않는다 */
        /*
        if (!force && TestUtil.isDebugMode())
            return
        */

        MLog.deb("SyncAdapter is called")
        if (!force) {
            /*

            서버 비용을 줄이기위해 6/1 확률로 요청..
            if (Random().nextInt(6) != 1)
                return

            */
        }

        if (force || (!isSyncing)) {
            val intent = Intent(context, SyncService::class.java)
            intent.putExtra(SyncService.KEY_ONLY_SYN_LOCAL, onlySyncLocal)
            context.startService(intent)
            isSyncing = true
        }

        postDelay({ finishSync() }, 30000)
    }

    fun finishSync() {
        isSyncing = false
    }

}
