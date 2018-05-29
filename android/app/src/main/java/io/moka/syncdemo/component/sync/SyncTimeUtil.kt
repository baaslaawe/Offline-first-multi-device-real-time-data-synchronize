package io.moka.syncdemo.component.sync


object SyncTimeUtil {

    private var timeGap: Long = 0

    fun setTimeGap(localTime: Long, serverTimestamp: Long): Long {
        return if (1L == localTime / 1000000000) {
            timeGap = serverTimestamp - localTime
            timeGap
        }
        else {
            0
        }
    }

    fun syncUpdatedAt(localUpdatedAt: Long): Long {
        return localUpdatedAt + timeGap
    }

}
