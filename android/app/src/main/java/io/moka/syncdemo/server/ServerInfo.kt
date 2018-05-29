package io.moka.syncdemo.server


object ServerInfo {

    private const val END_POINT_API_PROD = "http://192.168.123.18:3000/"

    val appEndPoint: String
        get() {
            return END_POINT_API_PROD
        }

    val ApiToken = "mokaisthebest1004"

}
