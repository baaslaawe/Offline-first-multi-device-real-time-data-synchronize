package io.moka.base.component

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import io.moka.mokabaselib.util.color
import io.moka.syncdemo.R

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Lifecycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        resizeFontSizeAndExt()
    }

    fun changeStatusBarColor(colorResId: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            window.statusBarColor = color(colorResId)
        }
    }

    /**
     */

    abstract fun resizeFontSizeAndExt()

    fun onBackPressedSlideOut() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right)
    }

}