package io.moka.syncdemo.component.preference

import io.moka.mokabaselib.preference.SharedPreferenceManager

object SDPref : SharedPreferenceManager("sync_demo_pref") {

    /* */

    private const val SYNCED_AT = "SyncDemo.SYNCED_AT"
    var syncedAt: Long
        get() = getExtraLong(SYNCED_AT, 0L)
        set(value) = setExtraLong(SYNCED_AT, value).run {}

}
