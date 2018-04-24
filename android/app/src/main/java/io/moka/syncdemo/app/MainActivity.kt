package io.moka.syncdemo.app

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.moka.base.component.BaseActivity
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
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val items = ArrayList<QnaAdapter.Data>()
        items.add(QnaAdapter.Data(1))
        items.add(QnaAdapter.Data(2))
        items.add(QnaAdapter.Data(3))
        adapter.items = items
    }

}
