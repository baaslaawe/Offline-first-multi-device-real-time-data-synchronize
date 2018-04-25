package io.moka.syncdemo.app

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.moka.base.component.BaseActivity
import io.moka.base.module.radius
import io.moka.syncdemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    val adapter by lazy { QnaAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        /* recyclerView */
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        /* profile image */
        imageView_profile.radius(this, R.drawable.vc_profile_gray, 32)

        /* coordinator layout */
        appBarlayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (isFinishing)
                return@addOnOffsetChangedListener
//            group_profile.alpha = (verticalOffset.toFloat() / appBarLayout.totalScrollRange) * -1
            imageView_profile.alpha = 1 - (verticalOffset.toFloat() / appBarLayout.totalScrollRange) * -1
            textView_say.alpha = 1 - (verticalOffset.toFloat() / appBarLayout.totalScrollRange) * -1
            textView_name.alpha = 1 - (verticalOffset.toFloat() / appBarLayout.totalScrollRange) * -1
        }

        val items = ArrayList<QnaAdapter.Data>()
        items.add(QnaAdapter.Data(1))
        items.add(QnaAdapter.Data(2))
        items.add(QnaAdapter.Data(3))
        items.add(QnaAdapter.Data(4))
        items.add(QnaAdapter.Data(4))
        items.add(QnaAdapter.Data(4))
        items.add(QnaAdapter.Data(4))
        adapter.items = items
    }

}
