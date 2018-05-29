package io.moka.syncdemo

import android.annotation.SuppressLint
import android.content.Context
import com.facebook.stetho.Stetho
import com.google.firebase.messaging.FirebaseMessaging
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
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

        FirebaseMessaging.getInstance().subscribeToTopic("general")

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                RealmInspectorModulesProvider
                                        .builder(this)
                                        .withLimit(100000)
                                        .build()
                        )
                        .build())
    }

}