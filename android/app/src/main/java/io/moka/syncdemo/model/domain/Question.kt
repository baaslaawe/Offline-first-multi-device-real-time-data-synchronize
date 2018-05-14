package io.moka.syncdemo.model.domain


import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey

open class Question(

        @PrimaryKey
        var id: Long = 0,
        var serverId: Long = 0,

        var name: String? = null,
        var say: String? = null,
        var imageUrl: String? = null,
        var question: String? = null,

        var dirtyFlag: Boolean = true,
        var createdAt: Long = 0,
        var updatedAt: Long = 0

) : RealmObject(), _BaseDomain

/**
 */

fun Question.copy(): Question {
    val question = Question()
    question.id = this.id
    question.serverId = this.serverId

    question.name = this.name
    question.say = this.say
    question.imageUrl = this.imageUrl
    question.question = this.question

    question.dirtyFlag = this.dirtyFlag
    question.createdAt = this.createdAt
    question.updatedAt = this.updatedAt
    return question
}

fun RealmResults<Question>.copy(): List<Question> {
    return this.map { it.copy() }
}