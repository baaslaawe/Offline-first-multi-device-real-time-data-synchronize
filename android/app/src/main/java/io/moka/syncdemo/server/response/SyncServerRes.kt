package io.moka.syncdemo.server.response


class SyncServerRes : BaseRes() {

    var error: String? = null
    lateinit var data: Data

    class Data {
        var result: Result? = null
        var syncAt: Long = 0
    }

    class Result {
        var postQuestion: List<PostRes>? = null
        var patchQuestion: List<PatchRes>? = null

        var postAnswer: List<PostRes>? = null
        var patchAnswer: List<PatchRes>? = null
    }

    class PatchRes {
        var local_id: Long = 0
    }

    class PostRes {
        var local_id: Long = 0
        var server_id: Long = 0
    }

}
