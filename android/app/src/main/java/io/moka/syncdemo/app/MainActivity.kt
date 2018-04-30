package io.moka.syncdemo.app

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.moka.base.component.BaseActivity
import io.moka.base.module.radius
import io.moka.syncdemo.R
import io.moka.syncdemo.app.dialog.AnswerDialog
import io.moka.syncdemo.app.dialog.PostQuestionDialog
import io.moka.syncdemo.model.domain.Qna
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk15.coroutines.onClick

class MainActivity : BaseActivity() {

    private val adapter by lazy { QnaAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        loadData()
    }

    private fun initView() {
        /* recyclerView */
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

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
        adapter.onClickItem = { onClickToAnswer(it) }

    }

    private fun loadData() {
        val items = ArrayList<QnaAdapter.Data>()
        items.add(QnaAdapter.Data())
        items.add(QnaAdapter.Data())
        items.add(QnaAdapter.Data())
        items.add(QnaAdapter.Data())
        items.add(QnaAdapter.Data())
        items.add(QnaAdapter.Data())
        items.add(QnaAdapter.Data())
        adapter.items = items
    }

    /*
     */

    private fun onClickToPostQuestion() {
        PostQuestionDialog()
                .showDialog(supportFragmentManager, { text: String ->

                })
    }

    private fun onClickToAnswer(qna: Qna?) {
        AnswerDialog()
                .apply {
                    question = qna?.question
                }
                .showDialog(supportFragmentManager, {

                })
    }

}
