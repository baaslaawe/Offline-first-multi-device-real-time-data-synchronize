package io.moka.syncdemo.model.domain


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Question(

        @PrimaryKey
        var id: Long = 0,
        var serverId: Long = 0,
        var userId: Long = 0,

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
    question.userId = this.userId

    question.question = this.question

    question.dirtyFlag = this.dirtyFlag
    question.createdAt = this.createdAt
    question.updatedAt = this.updatedAt
    return question
}
