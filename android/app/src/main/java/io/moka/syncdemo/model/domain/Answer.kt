package io.moka.syncdemo.model.domain


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Answer(

        @PrimaryKey
        var id: Long = 0,
        var serverId: Long = 0,
        var userId: Long = 0,
        var questionId: Long = 0,

        var answer: String? = null,

        var dirtyFlag: Boolean = true,
        var createdAt: Long = 0,
        var updatedAt: Long = 0

) : RealmObject(), _BaseDomain

/**
 */

fun Answer.copy(): Answer {
    val answer = Answer()
    answer.id = this.id
    answer.serverId = this.serverId
    answer.userId = this.userId
    answer.questionId = this.questionId

    answer.answer = this.answer

    answer.dirtyFlag = this.dirtyFlag
    answer.createdAt = this.createdAt
    answer.updatedAt = this.updatedAt
    return answer
}
