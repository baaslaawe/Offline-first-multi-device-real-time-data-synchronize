package io.moka.syncdemo.app.detail

import android.os.Bundle
import io.moka.base.component.BaseActivity
import io.moka.base.module.radius
import io.moka.syncdemo.R
import io.moka.syncdemo.app.main.dialog.PostQuestionDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk15.coroutines.onClick

class DetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        loadData()
    }

    private fun initView() {
        /* profile image */
        imageView_profile.radius(this, R.drawable.vc_profile_gray, 28)

        /* coordinator layout */
        appBarlayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (isFinishing)
                return@addOnOffsetChangedListener
            imageView_profile.alpha = 1 - (verticalOffset.toFloat() / appBarLayout.totalScrollRange) * -1
            textView_say.alpha = 1 - (verticalOffset.toFloat() / appBarLayout.totalScrollRange) * -1
            textView_name.alpha = 1 - (verticalOffset.toFloat() / appBarLayout.totalScrollRange) * -1
        }

        /* bind events */
        floatingActionButton_add.onClick { onClickToPostQuestion() }

    }

    private fun loadData() {
    }

    /*
     */

    private fun onClickToPostQuestion() {
        PostQuestionDialog()
                .showDialog(supportFragmentManager, { text: String ->

                })
    }

}
