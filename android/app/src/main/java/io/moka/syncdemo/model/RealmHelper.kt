package io.moka.syncdemo.model


import android.content.Context
import io.realm.Realm
import io.realm.RealmAsyncTask
import io.realm.RealmConfiguration


object RealmHelper {

    private const val DB_VERSION = 0 /* todo : DB 마이그레이션 할때 +1 해줘야됨 */

    fun init(context: Context) {
        Realm.init(context)
        val builder = RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(DB_VERSION.toLong())
                .deleteRealmIfMigrationNeeded()
                .migration(Migration())

        Realm.setDefaultConfiguration(builder.build())
    }

    val instance: Realm
        get() = Realm.getDefaultInstance()

    /**
     */

    fun onInstance(realm: Realm? = null, work: (realm: Realm) -> Unit) {
        val realmInstance: Realm = realm ?: instance

        work(realmInstance)

        if (realm == null)
            realmInstance.close()
    }

    fun onInstanceNoClose(realm: Realm? = null, work: (realm: Realm) -> Unit) {
        val realmInstance: Realm = realm ?: instance

        work(realmInstance)
    }

    /**
     */

    fun onTransaction(realm: Realm? = null, work: (realm: Realm) -> Unit) {
        val realmInstance: Realm = realm ?: instance

        realmInstance.beginTransaction()
        work(realmInstance)
        realmInstance.commitTransaction()

        if (realm == null)
            realmInstance.close()
    }

    fun onTransactionAsync(realm: Realm? = null, work: (realm: Realm) -> Unit, callback: () -> Unit): RealmAsyncTask {
        val realmInstance: Realm = realm ?: instance

        return realmInstance.executeTransactionAsync(
                {
                    work(it)
                },
                {
                    callback()

                    if (realm == null)
                        realmInstance.close()
                },
                {
                    it.printStackTrace()
                    if (realm == null)
                        realmInstance.close()
                }
        )
    }

}
