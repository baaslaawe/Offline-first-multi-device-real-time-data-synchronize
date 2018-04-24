package io.moka.syncdemo.model.dao.qna

import io.moka.syncdemo.model.RealmHelper
import io.moka.syncdemo.model.dao._BaseDao
import io.moka.syncdemo.model.domain.Qna
import io.moka.syncdemo.model.domain.copy
import io.moka.syncdemo.util.DateUtil
import io.realm.Realm
import io.realm.RealmAsyncTask

object QnaDao : _BaseDao<Qna> {

    /*
     * realm == null 이면 copy 하여 리턴, observer 를 통해 알린다
     * realm != null 이면 realm 객체를 리턴, observer 를 통해 알리지 않는다
     */

    /**
     * Insert
     */

    override fun insert(realm: Realm?, insert: (qna: Qna) -> Unit, syncFlag: Boolean): Qna {
        var copyQna: Qna? = null
        var realmQna: Qna? = null

        RealmHelper.onTransaction(realm = realm, work = { realmInstance ->

            val number = realmInstance.where(Qna::class.java).max("id")
            val qnaId: Long
            if (null == number)
                qnaId = 1
            else
                qnaId = number.toLong() + 1

            val qna = realmInstance.createObject(Qna::class.java, qnaId)

            /* 기본값 지정 */

            insert(qna)
            switchSyncFlag(qna, syncFlag)
            qna.createdAt = DateUtil.timestampInSecond
            qna.updatedAt = DateUtil.timestampInSecond

            if (null == realm)
                copyQna = qna.copy()
            else
                realmQna = qna
        })

        if (null != copyQna) {
            if (!syncFlag) {
//                SyncAdapter.performSync()
            }
            return copyQna!!
        }
        else {
            return realmQna!!
        }
    }

    override fun insertAsync(realm: Realm?, insert: (qna: Qna) -> Unit, callback: (qna: Qna) -> Unit, syncFlag: Boolean): RealmAsyncTask {
        var copyQna: Qna? = null
        var realmQna: Qna? = null

        return RealmHelper.onTransactionAsync(realm = realm,
                work = { realmInstance ->

                    val number = realmInstance.where(Qna::class.java).max("id")
                    val qnaId: Long
                    if (null == number)
                        qnaId = 1
                    else
                        qnaId = number.toLong() + 1

                    val qna = realmInstance.createObject(Qna::class.java, qnaId)

                    /* 기본값 지정 */

                    insert(qna)
                    switchSyncFlag(qna, syncFlag)
                    qna.createdAt = DateUtil.timestampInSecond
                    qna.updatedAt = DateUtil.timestampInSecond

                    if (null == realm)
                        copyQna = qna.copy()
                    else
                        realmQna = qna
                },
                callback = {
                    if (null == realm) {
                        if (!syncFlag) {
//                            SyncAdapter.performSync()
                        }
                        callback(copyQna!!)
                    }
                    else
                        callback(realmQna!!)
                })
    }

    /**
     * Update
     */

    override fun update(realm: Realm?, id: Long?, domain: Qna?, update: (qna: Qna) -> Unit, syncFlag: Boolean): Qna {
        var copyQna: Qna? = null
        var realmQna: Qna? = null

        RealmHelper.onTransaction(realm = realm, work = { realmInstance ->

            var qna: Qna? = null
            when {
                null != domain -> qna = realmInstance.copyToRealmOrUpdate(qna)
                null != id -> qna = realmInstance.where(Qna::class.java).equalTo("id", id).findFirst()
                else -> RuntimeException("both (id / qna) must not be null")
            }

            update(qna!!)
            switchSyncFlag(qna, syncFlag)
            qna.updatedAt = DateUtil.timestampInSecond

            if (null == realm)
                copyQna = qna.copy()
            else
                realmQna = qna
        })

        if (null != copyQna) {
            if (!syncFlag) {
//                SyncAdapter.performSync()
            }
            return copyQna!!
        }
        else {
            return realmQna!!
        }
    }

    override fun updateAsync(realm: Realm?, id: Long?, domain: Qna?, update: (qna: Qna) -> Unit, callback: (qna: Qna) -> Unit, syncFlag: Boolean): RealmAsyncTask {
        var copyQna: Qna? = null
        var realmQna: Qna? = null

        return RealmHelper.onTransactionAsync(realm = realm,
                work = { realmInstance ->

                    var qna: Qna? = null
                    if (null != domain)
                        qna = realmInstance.copyToRealmOrUpdate(qna)
                    else if (null != id)
                        qna = realmInstance.where(Qna::class.java).equalTo("id", id).findFirst()
                    else
                        RuntimeException("both (id / qna) must not be null")

                    update(qna!!)
                    switchSyncFlag(qna, syncFlag)
                    qna.updatedAt = DateUtil.timestampInSecond

                    if (null == realm)
                        copyQna = qna.copy()
                    else
                        realmQna = qna
                },
                callback = {
                    if (null != copyQna) {
                        if (!syncFlag) {
//                            SyncAdapter.performSync()
                        }
                        callback(copyQna!!)
                    }
                    else {
                        callback(realmQna!!)
                    }
                })
    }

    /**
     * Delete
     */

    override fun delete(realm: Realm?, id: Long?, domain: Qna?) {
        var copyQna: Qna? = null
        RealmHelper.onTransaction { realm ->

            val realmQna: Qna? = when {
                null != domain -> realm.copyToRealmOrUpdate(domain)
                null != id -> realm.where(Qna::class.java).equalTo("id", id).findFirst()
                else -> throw RuntimeException("both (id / qna) must not be null")
            }

            copyQna = realmQna?.copy()
            realmQna?.deleteFromRealm()
        }
    }

    /**
     * Get
     */

    override fun get(realm: Realm?, id: Long): Qna? {
        var copyQna: Qna? = null
        var realmQna: Qna? = null

        RealmHelper.onInstance(realm = realm, work = { realmInstance ->
            val qna = realmInstance.where(Qna::class.java)
                    .equalTo("id", id)
                    .findFirst()

            if (null == realm)
                copyQna = qna?.copy()
            else
                realmQna = qna
        })

        return if (null == realm)
            copyQna
        else
            realmQna
    }

    override fun getAsync(realm: Realm?, id: Long, callback: (qna: Qna?) -> Unit) {
        RealmHelper.onInstanceNoClose(realm = realm, work = { realmInstance ->

            realmInstance.where(Qna::class.java)
                    .equalTo("id", id)
                    .findFirstAsync()
                    .run {
                        addChangeListener(
                                { qna: Qna? ->
                                    if (!isValid) {
                                        callback(null)
                                        return@addChangeListener
                                    }

                                    if (null == realm) {
                                        callback(qna?.copy())
                                        realmInstance.close()
                                    }
                                    else {
                                        callback(qna)
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

    private fun switchSyncFlag(qna: Qna, syncFlag: Boolean) {
        qna.dirtyFlag = !syncFlag
    }

}
