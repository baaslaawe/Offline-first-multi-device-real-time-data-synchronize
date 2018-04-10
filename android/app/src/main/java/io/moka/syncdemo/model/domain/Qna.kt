package io.moka.syncdemo.model.domain


import io.moka.dayday_alrm.model.domain._BaseDomain
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Qna(

        @PrimaryKey
        var id: Long = 0,
        var serverId: Long = 0,

        var dirtyFlag: Boolean = true,
        var createdAt: Long = 0,
        var updatedAt: Long = 0

) : RealmObject(), _BaseDomain

/**
 */

fun Qna.copy(): Qna {
    val qna = Qna()
    qna.id = this.id
    qna.serverId = this.serverId

    qna.dirtyFlag = this.dirtyFlag
    qna.createdAt = this.createdAt
    qna.updatedAt = this.updatedAt
    return qna
}
