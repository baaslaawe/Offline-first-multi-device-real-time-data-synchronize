package io.moka.syncdemo.util

import io.moka.syncdemo.BuildConfig


object TestUtil {

    val isDebugMode = BuildConfig.DEBUG

    val isReleaseMode: Boolean
        get() = !isDebugMode

    val isDev: Boolean
        get() = BuildConfig.FLAVOR == "dev"

    val isProd: Boolean
        get() = BuildConfig.FLAVOR == "prod"

}
