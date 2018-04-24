package io.moka.syncdemo.model.dao

import io.moka.syncdemo.model.domain._BaseDomain
import io.realm.Realm
import io.realm.RealmAsyncTask

interface _BaseDao<T : _BaseDomain> {

    fun insert(realm: Realm? = null, insert: (domain: T) -> Unit, syncFlag: Boolean = false): T

    fun insertAsync(realm: Realm? = null, insert: (domain: T) -> Unit, callback: (domain: T) -> Unit, syncFlag: Boolean = false): RealmAsyncTask

    fun update(realm: Realm? = null, id: Long?, domain: T?, update: (domain: T) -> Unit, syncFlag: Boolean = false): T

    fun updateAsync(realm: Realm?, id: Long?, domain: T?, update: (domain: T) -> Unit, callback: (domain: T) -> Unit, syncFlag: Boolean = false): RealmAsyncTask

    fun delete(realm: Realm? = null, id: Long?, domain: T?)

    fun get(realm: Realm? = null, id: Long): T?

    fun getAsync(realm: Realm?, id: Long, callback: (domain: T?) -> Unit)

}