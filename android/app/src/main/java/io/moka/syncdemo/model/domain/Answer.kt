package io.moka.syncdemo.model.domain


import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey

open class Answer(

        @PrimaryKey
        var id: Long = 0,
        var serverId: Long = 0,
        var questionServerId: Long = 0,
        var questionId: Long = 0,

        var name: String? = null,
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
    answer.questionServerId = this.questionServerId

    answer.name = this.name
    answer.questionId = this.questionId

    answer.answer = this.answer

    answer.dirtyFlag = this.dirtyFlag
    answer.createdAt = this.createdAt
    answer.updatedAt = this.updatedAt
    return answer
}

fun RealmResults<Answer>.copy(): List<Answer> {
    return this.map { it.copy() }
}