package io.moka.syncdemo.model.dao.user

import io.moka.syncdemo.model.RealmHelper
import io.moka.syncdemo.model.dao._BaseDao
import io.moka.syncdemo.model.domain.User
import io.moka.syncdemo.model.domain.copy
import io.moka.syncdemo.util.DateUtil
import io.realm.Realm
import io.realm.RealmAsyncTask

object UserDao : _BaseDao<User> {

    /*
     * realm == null 이면 copy 하여 리턴, observer 를 통해 알린다
     * realm != null 이면 realm 객체를 리턴, observer 를 통해 알리지 않는다
     */

    /**
     * Insert
     */

    override fun insert(realm: Realm?, insert: (user: User) -> Unit, syncFlag: Boolean): User {
        var copyUser: User? = null
        var realmUser: User? = null

        RealmHelper.onTransaction(realm = realm, work = { realmInstance ->

            val number = realmInstance.where(User::class.java).max("id")
            val userId: Long
            userId = if (null == number)
                1
            else
                number.toLong() + 1

            val user = realmInstance.createObject(User::class.java, userId)

            /* 기본값 지정 */

            insert(user)
            switchSyncFlag(user, syncFlag)
            user.createdAt = DateUtil.timestampInSecond
            user.updatedAt = DateUtil.timestampInSecond

            if (null == realm)
                copyUser = user.copy()
            else
                realmUser = user
        })

        if (null != copyUser) {
            if (!syncFlag) {
//                SyncAdapter.performSync()
            }
            return copyUser!!
        }
        else {
            return realmUser!!
        }
    }

    override fun insertAsync(realm: Realm?, insert: (user: User) -> Unit, callback: (user: User) -> Unit, syncFlag: Boolean): RealmAsyncTask {
        var copyUser: User? = null
        var realmUser: User? = null

        return RealmHelper.onTransactionAsync(realm = realm,
                work = { realmInstance ->

                    val number = realmInstance.where(User::class.java).max("id")
                    val userId: Long
                    userId = if (null == number)
                        1
                    else
                        number.toLong() + 1

                    val user = realmInstance.createObject(User::class.java, userId)

                    /* 기본값 지정 */

                    insert(user)
                    switchSyncFlag(user, syncFlag)
                    user.createdAt = DateUtil.timestampInSecond
                    user.updatedAt = DateUtil.timestampInSecond

                    if (null == realm)
                        copyUser = user.copy()
                    else
                        realmUser = user
                },
                callback = {
                    if (null == realm) {
                        if (!syncFlag) {
//                            SyncAdapter.performSync()
                        }
                        callback(copyUser!!)
                    }
                    else
                        callback(realmUser!!)
                })
    }

    /**
     * Update
     */

    override fun update(realm: Realm?, id: Long?, domain: User?, update: (user: User) -> Unit, syncFlag: Boolean): User {
        var copyUser: User? = null
        var realmUser: User? = null

        RealmHelper.onTransaction(realm = realm, work = { realmInstance ->

            var user: User? = null
            when {
                null != domain -> user = realmInstance.copyToRealmOrUpdate(domain)
                null != id -> user = realmInstance.where(User::class.java).equalTo("id", id).findFirst()
                else -> RuntimeException("both (id / user) must not be null")
            }

            update(user!!)
            switchSyncFlag(user, syncFlag)
            user.updatedAt = DateUtil.timestampInSecond

            if (null == realm)
                copyUser = user.copy()
            else
                realmUser = user
        })

        if (null != copyUser) {
            if (!syncFlag) {
//                SyncAdapter.performSync()
            }
            return copyUser!!
        }
        else {
            return realmUser!!
        }
    }

    override fun updateAsync(realm: Realm?, id: Long?, domain: User?, update: (user: User) -> Unit, callback: (user: User) -> Unit, syncFlag: Boolean): RealmAsyncTask {
        var copyUser: User? = null
        var realmUser: User? = null

        return RealmHelper.onTransactionAsync(realm = realm,
                work = { realmInstance ->

                    var user: User? = null
                    when {
                        null != domain -> user = realmInstance.copyToRealmOrUpdate(domain)
                        null != id -> user = realmInstance.where(User::class.java).equalTo("id", id).findFirst()
                        else -> RuntimeException("both (id / user) must not be null")
                    }

                    update(user!!)
                    switchSyncFlag(user, syncFlag)
                    user.updatedAt = DateUtil.timestampInSecond

                    if (null == realm)
                        copyUser = user.copy()
                    else
                        realmUser = user
                },
                callback = {
                    if (null != copyUser) {
                        if (!syncFlag) {
//                            SyncAdapter.performSync()
                        }
                        callback(copyUser!!)
                    }
                    else {
                        callback(realmUser!!)
                    }
                })
    }

    /**
     * Delete
     */

    override fun delete(realm: Realm?, id: Long?, domain: User?) {
        var copyUser: User? = null
        RealmHelper.onTransaction { realm ->

            val realmUser: User? = when {
                null != domain -> realm.copyToRealmOrUpdate(domain)
                null != id -> realm.where(User::class.java).equalTo("id", id).findFirst()
                else -> throw RuntimeException("both (id / user) must not be null")
            }

            copyUser = realmUser?.copy()
            realmUser?.deleteFromRealm()
        }
    }

    /**
     * Get
     */

    override fun get(realm: Realm?, id: Long): User? {
        var copyUser: User? = null
        var realmUser: User? = null

        RealmHelper.onInstance(realm = realm, work = { realmInstance ->
            val user = realmInstance.where(User::class.java)
                    .equalTo("id", id)
                    .findFirst()

            if (null == realm)
                copyUser = user?.copy()
            else
                realmUser = user
        })

        return if (null == realm)
            copyUser
        else
            realmUser
    }

    override fun getAsync(realm: Realm?, id: Long, callback: (user: User?) -> Unit) {
        RealmHelper.onInstanceNoClose(realm = realm, work = { realmInstance ->

            realmInstance.where(User::class.java)
                    .equalTo("id", id)
                    .findFirstAsync()
                    .run {
                        addChangeListener(
                                { user: User? ->
                                    if (!isValid) {
                                        callback(null)
                                        return@addChangeListener
                                    }

                                    if (null == realm) {
                                        callback(user?.copy())
                                        realmInstance.close()
                                    }
                                    else {
                                        callback(user)
                                    }
                                })

                        this.removeAllChangeListeners()
                    }
        })
    }

    /**
     */

    /**
     * syncFlag = true :: 서버와 동기화가 되었다. dirtyFlag 를 false 로
     * syncFlag = false :: 서버와 동기화되지 않아 동기화 로직을 통해 동기화를 해야한다. dirtyFlag 를 true 로
     */

    private fun switchSyncFlag(user: User, syncFlag: Boolean) {
        user.dirtyFlag = !syncFlag
    }

}
