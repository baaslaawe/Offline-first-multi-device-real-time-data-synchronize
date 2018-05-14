package io.moka.syncdemo.component

import io.moka.mokabaselib.preference.SharedPreferenceManager


object UserManager : SharedPreferenceManager("UserManager") {

    private const val KEY_CURRENT_NAME = "app.KEY_CURRENT_NAME"
    var currentName: String = "init"
        get() {
            return if (field.contentEquals("init")) {
                field = getExtraString(KEY_CURRENT_NAME, "")
                field
            }
            else {
                field
            }
        }
        set(currentName) {
            field = currentName
            setExtraString(KEY_CURRENT_NAME, currentName)
        }

    private const val KEY_CURRENT_SAYING = "app.KEY_CURRENT_SAYING"
    var currentSay: String = "init"
        get() {
            return if (field.contentEquals("init")) {
                field = getExtraString(KEY_CURRENT_SAYING, "")
                field
            }
            else {
                field
            }
        }
        set(currentSaying) {
            field = currentSaying
            setExtraString(KEY_CURRENT_SAYING, currentSaying)
        }

    private const val KEY_CURRENT_IMAGE_URL = "app.KEY_CURRENT_IMAGE_URL"
    var currentImageUrl: String = "init"
        get() {
            return if (field.contentEquals("init")) {
                field = getExtraString(KEY_CURRENT_IMAGE_URL, "")
                field
            }
            else {
                field
            }
        }
        set(currentImageUrl) {
            field = currentImageUrl
            setExtraString(KEY_CURRENT_IMAGE_URL, currentImageUrl)
        }

}