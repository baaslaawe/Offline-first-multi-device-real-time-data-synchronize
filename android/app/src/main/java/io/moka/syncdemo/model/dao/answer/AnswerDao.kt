package io.moka.syncdemo.model.dao.answer

import io.moka.syncdemo.model.RealmHelper
import io.moka.syncdemo.model.dao._BaseDao
import io.moka.syncdemo.model.domain.Answer
import io.moka.syncdemo.model.domain.copy
import io.moka.syncdemo.util.DateUtil
import io.realm.Realm
import io.realm.RealmAsyncTask

object AnswerDao : _BaseDao<Answer> {

    /*
     * realm == null 이면 copy 하여 리턴, observer 를 통해 알린다
     * realm != null 이면 realm 객체를 리턴, observer 를 통해 알리지 않는다
     */

    /**
     * Insert
     */

    override fun insert(realm: Realm?, insert: (answer: Answer) -> Unit, syncFlag: Boolean): Answer {
        var copyAnswer: Answer? = null
        var realmAnswer: Answer? = null

        RealmHelper.onTransaction(realm = realm, work = { realmInstance ->

            val number = realmInstance.where(Answer::class.java).max("id")
            val answerId: Long
            answerId = if (null == number)
                1
            else
                number.toLong() + 1

            val answer = realmInstance.createObject(Answer::class.java, answerId)

            /* 기본값 지정 */

            insert(answer)
            switchSyncFlag(answer, syncFlag)
            answer.createdAt = DateUtil.timestampInSecond
            answer.updatedAt = DateUtil.timestampInSecond

            if (null == realm)
                copyAnswer = answer.copy()
            else
                realmAnswer = answer
        })

        if (null != copyAnswer) {
            if (!syncFlag) {
//                SyncAdapter.performSync()
            }
            return copyAnswer!!
        }
        else {
            return realmAnswer!!
        }
    }

    override fun insertAsync(realm: Realm?, insert: (answer: Answer) -> Unit, callback: (answer: Answer) -> Unit, syncFlag: Boolean): RealmAsyncTask {
        var copyAnswer: Answer? = null
        var realmAnswer: Answer? = null

        return RealmHelper.onTransactionAsync(realm = realm,
                work = { realmInstance ->

                    val number = realmInstance.where(Answer::class.java).max("id")
                    val answerId: Long
                    answerId = if (null == number)
                        1
                    else
                        number.toLong() + 1

                    val answer = realmInstance.createObject(Answer::class.java, answerId)

                    /* 기본값 지정 */

                    insert(answer)
                    switchSyncFlag(answer, syncFlag)
                    answer.createdAt = DateUtil.timestampInSecond
                    answer.updatedAt = DateUtil.timestampInSecond

                    if (null == realm)
                        copyAnswer = answer.copy()
                    else
                        realmAnswer = answer
                },
                callback = {
                    if (null == realm) {
                        if (!syncFlag) {
//                            SyncAdapter.performSync()
                        }
                        callback(copyAnswer!!)
                    }
                    else
                        callback(realmAnswer!!)
                })
    }

    /**
     * Update
     */

    override fun update(realm: Realm?, id: Long?, domain: Answer?, update: (answer: Answer) -> Unit, syncFlag: Boolean): Answer {
        var copyAnswer: Answer? = null
        var realmAnswer: Answer? = null

        RealmHelper.onTransaction(realm = realm, work = { realmInstance ->

            var answer: Answer? = null
            when {
                null != domain -> answer = realmInstance.copyToRealmOrUpdate(domain)
                null != id -> answer = realmInstance.where(Answer::class.java).equalTo("id", id).findFirst()
                else -> RuntimeException("both (id / answer) must not be null")
            }

            update(answer!!)
            switchSyncFlag(answer, syncFlag)
            answer.updatedAt = DateUtil.timestampInSecond

            if (null == realm)
                copyAnswer = answer.copy()
            else
                realmAnswer = answer
        })

        if (null != copyAnswer) {
            if (!syncFlag) {
//                SyncAdapter.performSync()
            }
            return copyAnswer!!
        }
        else {
            return realmAnswer!!
        }
    }

    override fun updateAsync(realm: Realm?, id: Long?, domain: Answer?, update: (answer: Answer) -> Unit, callback: (answer: Answer) -> Unit, syncFlag: Boolean): RealmAsyncTask {
        var copyAnswer: Answer? = null
        var realmAnswer: Answer? = null

        return RealmHelper.onTransactionAsync(realm = realm,
                work = { realmInstance ->

                    var answer: Answer? = null
                    when {
                        null != domain -> answer = realmInstance.copyToRealmOrUpdate(domain)
                        null != id -> answer = realmInstance.where(Answer::class.java).equalTo("id", id).findFirst()
                        else -> RuntimeException("both (id / answer) must not be null")
                    }

                    update(answer!!)
                    switchSyncFlag(answer, syncFlag)
                    answer.updatedAt = DateUtil.timestampInSecond

                    if (null == realm)
                        copyAnswer = answer.copy()
                    else
                        realmAnswer = answer
                },
                callback = {
                    if (null != copyAnswer) {
                        if (!syncFlag) {
//                            SyncAdapter.performSync()
                        }
                        callback(copyAnswer!!)
                    }
                    else {
                        callback(realmAnswer!!)
                    }
                })
    }

    /**
     * Delete
     */

    override fun delete(realm: Realm?, id: Long?, domain: Answer?) {
        var copyAnswer: Answer? = null
        RealmHelper.onTransaction { realm ->

            val realmAnswer: Answer? = when {
                null != domain -> realm.copyToRealmOrUpdate(domain)
                null != id -> realm.where(Answer::class.java).equalTo("id", id).findFirst()
                else -> throw RuntimeException("both (id / answer) must not be null")
            }

            copyAnswer = realmAnswer?.copy()
            realmAnswer?.deleteFromRealm()
        }
    }

    /**
     * Get
     */

    override fun get(realm: Realm?, id: Long): Answer? {
        var copyAnswer: Answer? = null
        var realmAnswer: Answer? = null

        RealmHelper.onInstance(realm = realm, work = { realmInstance ->
            val answer = realmInstance.where(Answer::class.java)
                    .equalTo("id", id)
                    .findFirst()

            if (null == realm)
                copyAnswer = answer?.copy()
            else
                realmAnswer = answer
        })

        return if (null == realm)
            copyAnswer
        else
            realmAnswer
    }

    override fun getAsync(realm: Realm?, id: Long, callback: (answer: Answer?) -> Unit) {
        RealmHelper.onInstanceNoClose(realm = realm, work = { realmInstance ->

            realmInstance.where(Answer::class.java)
                    .equalTo("id", id)
                    .findFirstAsync()
                    .run {
                        addChangeListener(
                                { answer: Answer? ->
                                    if (!isValid) {
                                        callback(null)
                                        return@addChangeListener
                                    }

                                    if (null == realm) {
                                        callback(answer?.copy())
                                        realmInstance.close()
                                    }
                                    else {
                                        callback(answer)
                                    }
                                })

                        this.removeAllChangeListeners()
                    }
        })
    }

    fun getByQuestion(questionId: Long): List<Answer>? {
        var copyAnswers: List<Answer>? = null

        RealmHelper.onInstance(realm = null, work = { realmInstance ->
            val realmResults = realmInstance.where(Answer::class.java)
                    .equalTo("questionId", questionId)
                    .findAll()

            copyAnswers = realmResults?.copy()
        })

        return copyAnswers
    }

    /**
     */

    /**
     * syncFlag = true :: 서버와 동기화가 되었다. dirtyFlag 를 false 로
     * syncFlag = false :: 서버와 동기화되지 않아 동기화 로직을 통해 동기화를 해야한다. dirtyFlag 를 true 로
     */

    private fun switchSyncFlag(answer: Answer, syncFlag: Boolean) {
        answer.dirtyFlag = !syncFlag
    }

}
