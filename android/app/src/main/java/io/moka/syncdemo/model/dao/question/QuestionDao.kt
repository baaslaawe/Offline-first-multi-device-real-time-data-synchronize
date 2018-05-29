package io.moka.syncdemo.model.dao.question

import io.moka.syncdemo.component.sync.SyncAdapter
import io.moka.syncdemo.model.RealmHelper
import io.moka.syncdemo.model.dao._BaseDao
import io.moka.syncdemo.model.domain.Question
import io.moka.syncdemo.model.domain.copy
import io.moka.syncdemo.util.DateUtil
import io.realm.Realm
import io.realm.RealmAsyncTask
import io.realm.RealmResults
import io.realm.Sort

object QuestionDao : _BaseDao<Question> {

    /*
     * realm == null 이면 copy 하여 리턴, observer 를 통해 알린다
     * realm != null 이면 realm 객체를 리턴, observer 를 통해 알리지 않는다
     */

    /**
     * Insert
     */

    override fun insert(realm: Realm?, insert: (question: Question) -> Unit, syncFlag: Boolean): Question {
        var copyQuestion: Question? = null
        var realmQuestion: Question? = null

        RealmHelper.onTransaction(realm = realm, work = { realmInstance ->

            val number = realmInstance.where(Question::class.java).max("id")
            val questionId: Long
            questionId = if (null == number)
                1
            else
                number.toLong() + 1

            val question = realmInstance.createObject(Question::class.java, questionId)

            /* 기본값 지정 */

            insert(question)
            switchSyncFlag(question, syncFlag)
            question.createdAt = DateUtil.timestampInSecond
            question.updatedAt = DateUtil.timestampInSecond

            if (null == realm)
                copyQuestion = question.copy()
            else
                realmQuestion = question
        })

        if (null != copyQuestion) {
            if (!syncFlag) {
                SyncAdapter.performSync()
            }
            return copyQuestion!!
        }
        else {
            return realmQuestion!!
        }
    }

    override fun insertAsync(realm: Realm?, insert: (question: Question) -> Unit, callback: (question: Question) -> Unit, syncFlag: Boolean): RealmAsyncTask {
        var copyQuestion: Question? = null
        var realmQuestion: Question? = null

        return RealmHelper.onTransactionAsync(realm = realm,
                work = { realmInstance ->

                    val number = realmInstance.where(Question::class.java).max("id")
                    val questionId: Long
                    questionId = if (null == number)
                        1
                    else
                        number.toLong() + 1

                    val question = realmInstance.createObject(Question::class.java, questionId)

                    /* 기본값 지정 */

                    insert(question)
                    switchSyncFlag(question, syncFlag)
                    question.createdAt = DateUtil.timestampInSecond
                    question.updatedAt = DateUtil.timestampInSecond

                    if (null == realm)
                        copyQuestion = question.copy()
                    else
                        realmQuestion = question
                },
                callback = {
                    if (null == realm) {
                        if (!syncFlag) {
                            SyncAdapter.performSync()
                        }
                        callback(copyQuestion!!)
                    }
                    else
                        callback(realmQuestion!!)
                })
    }

    /**
     * Update
     */

    override fun update(realm: Realm?, id: Long?, domain: Question?, update: (question: Question) -> Unit, syncFlag: Boolean): Question {
        var copyQuestion: Question? = null
        var realmQuestion: Question? = null

        RealmHelper.onTransaction(realm = realm, work = { realmInstance ->

            var question: Question? = null
            when {
                null != domain -> question = realmInstance.copyToRealmOrUpdate(domain)
                null != id -> question = realmInstance.where(Question::class.java).equalTo("id", id).findFirst()
                else -> RuntimeException("both (id / question) must not be null")
            }

            update(question!!)
            switchSyncFlag(question, syncFlag)
            question.updatedAt = DateUtil.timestampInSecond

            if (null == realm)
                copyQuestion = question.copy()
            else
                realmQuestion = question
        })

        if (null != copyQuestion) {
            if (!syncFlag) {
                SyncAdapter.performSync()
            }
            return copyQuestion!!
        }
        else {
            return realmQuestion!!
        }
    }

    override fun updateAsync(realm: Realm?, id: Long?, domain: Question?, update: (question: Question) -> Unit, callback: (question: Question) -> Unit, syncFlag: Boolean): RealmAsyncTask {
        var copyQuestion: Question? = null
        var realmQuestion: Question? = null

        return RealmHelper.onTransactionAsync(realm = realm,
                work = { realmInstance ->

                    var question: Question? = null
                    when {
                        null != domain -> question = realmInstance.copyToRealmOrUpdate(domain)
                        null != id -> question = realmInstance.where(Question::class.java).equalTo("id", id).findFirst()
                        else -> RuntimeException("both (id / question) must not be null")
                    }

                    update(question!!)
                    switchSyncFlag(question, syncFlag)
                    question.updatedAt = DateUtil.timestampInSecond

                    if (null == realm)
                        copyQuestion = question.copy()
                    else
                        realmQuestion = question
                },
                callback = {
                    if (null != copyQuestion) {
                        if (!syncFlag) {
                            SyncAdapter.performSync()
                        }
                        callback(copyQuestion!!)
                    }
                    else {
                        callback(realmQuestion!!)
                    }
                })
    }

    /**
     * Delete
     */

    override fun delete(realm: Realm?, id: Long?, domain: Question?) {
        var copyQuestion: Question? = null
        RealmHelper.onTransaction { realm ->

            val realmQuestion: Question? = when {
                null != domain -> realm.copyToRealmOrUpdate(domain)
                null != id -> realm.where(Question::class.java).equalTo("id", id).findFirst()
                else -> throw RuntimeException("both (id / question) must not be null")
            }

            copyQuestion = realmQuestion?.copy()
            realmQuestion?.deleteFromRealm()
        }
    }

    /**
     * Get
     */

    override fun get(realm: Realm?, id: Long): Question? {
        var copyQuestion: Question? = null
        var realmQuestion: Question? = null

        RealmHelper.onInstance(realm = realm, work = { realmInstance ->
            val question = realmInstance.where(Question::class.java)
                    .equalTo("id", id)
                    .findFirst()

            if (null == realm)
                copyQuestion = question?.copy()
            else
                realmQuestion = question
        })

        return if (null == realm)
            copyQuestion
        else
            realmQuestion
    }

    override fun getAsync(realm: Realm?, id: Long, callback: (question: Question?) -> Unit) {
        RealmHelper.onInstanceNoClose(realm = realm, work = { realmInstance ->

            realmInstance.where(Question::class.java)
                    .equalTo("id", id)
                    .findFirstAsync()
                    .run {
                        addChangeListener(
                                { question: Question? ->
                                    if (!isValid) {
                                        callback(null)
                                        return@addChangeListener
                                    }

                                    if (null == realm) {
                                        callback(question?.copy())
                                        realmInstance.close()
                                    }
                                    else {
                                        callback(question)
                                    }
                                })

                        this.removeAllChangeListeners()
                    }
        })
    }

    fun getAll(): List<Question>? {
        var copyDayList: List<Question>? = null

        RealmHelper.onInstance(realm = null, work = { realmInstance ->
            val realmResults =
                    realmInstance.where(Question::class.java).sort("createdAt", Sort.DESCENDING).findAll()

            copyDayList = realmResults?.copy()
        })

        return copyDayList
    }

    fun getAllAsync(realm: Realm?, callback: (questions: List<Question>?) -> Unit) {
        RealmHelper.onInstanceNoClose(realm = realm, work = { realmInstance ->

            realmInstance.where(Question::class.java)
                    .findAllAsync()
                    .run {
                        addChangeListener(
                                { questions: RealmResults<Question>? ->
                                    if (!isValid) {
                                        callback(null)
                                        return@addChangeListener
                                    }

                                    if (null == realm) {
                                        callback(questions?.copy())
                                        realmInstance.close()
                                    }
                                    else {
                                        callback(questions)
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

    private fun switchSyncFlag(question: Question, syncFlag: Boolean) {
        question.dirtyFlag = !syncFlag
    }

}
