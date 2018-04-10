package io.moka.syncdemo.server.response


class SyncLocalRes : BaseRes() {

    var error: String? = null
    lateinit var data: Data

    class Data {

        var result: Result? = null
        var syncAt: Long = 0

    }

    class Result {

    }

}
