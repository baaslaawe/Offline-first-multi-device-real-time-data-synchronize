package io.moka.syncdemo.server


object ServerInfo {

    private const val END_POINT_API_PROD = "http://moka-a.io:3000/"

    val appEndPoint: String
        get() {
            return END_POINT_API_PROD
        }

    val ApiToken = "mokaisthebest1004"

}
