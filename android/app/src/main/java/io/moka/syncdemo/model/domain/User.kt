package io.moka.syncdemo.model.domain


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(

        @PrimaryKey
        var id: Long = 0,
        var serverId: Long = 0,

        var name: String? = null,
        var saying: String? = null,

        var dirtyFlag: Boolean = true,
        var createdAt: Long = 0,
        var updatedAt: Long = 0

) : RealmObject(), _BaseDomain

/**
 */

fun User.copy(): User {
    val user = User()
    user.id = this.id
    user.serverId = this.serverId

    user.name = this.name
    user.saying = this.saying

    user.dirtyFlag = this.dirtyFlag
    user.createdAt = this.createdAt
    user.updatedAt = this.updatedAt
    return user
}
