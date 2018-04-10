package io.moka.syncdemo.server.response


class SyncServerRes : BaseRes() {

    var error: String? = null
    lateinit var data: Data

    class Data {
        var result: Result? = null
        var syncAt: Long = 0
    }

    class Result {
        var postQnA: List<PostRes>? = null
        var patchQnA: List<PatchRes>? = null
    }

    class PatchRes {
        var local_id: Long = 0
    }

    class PostRes {
        var local_id: Long = 0
        var server_id: Long = 0
    }

}
