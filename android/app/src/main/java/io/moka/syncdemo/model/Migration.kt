package io.moka.syncdemo.model


import io.moka.mokabaselib.util.log.MLog
import io.realm.DynamicRealm
import io.realm.RealmMigration


class Migration : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var oldVersion = oldVersion
        MLog.deb("oldVersion : $oldVersion / newVersion : $newVersion")

        if (oldVersion == 0L) {
            migrate0To1(realm)
            oldVersion++
        }
    }

    private fun migrate0To1(realm: DynamicRealm) {
        val schema = realm.schema
    }

    private fun migrate1To2(realm: DynamicRealm) {
    }

}
