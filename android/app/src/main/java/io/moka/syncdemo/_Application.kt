package io.moka.syncdemo

import android.annotation.SuppressLint
import android.content.Context
import io.moka.base.component.BaseApplication
import io.moka.mokabaselib.MokaBase
import io.moka.syncdemo.model.RealmHelper
import io.moka.syncdemo.util.TestUtil

class _Application : BaseApplication() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        RealmHelper.init(this)

        MokaBase.context = this
        MokaBase.debuggable = TestUtil.isDebugMode
    }

}