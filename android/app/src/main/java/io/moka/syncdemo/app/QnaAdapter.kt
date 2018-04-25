package io.moka.syncdemo.app

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.moka.base.module.radius
import io.moka.mokabaselib.adapter.BaseAdapter
import io.moka.mokabaselib.adapter.ItemData
import io.moka.mokabaselib.adapter.RecyclerItemView
import io.moka.syncdemo.R
import io.moka.syncdemo.model.domain.Qna
import kotlinx.android.synthetic.main.view_qna_item.view.*

class QnaAdapter(private val context: Context) : BaseAdapter<QnaAdapter.Data, RecyclerItemView<QnaAdapter.Data>>() {

    override fun onCreateContentItemViewHolder(parent: ViewGroup, contentViewType: Int): RecyclerView.ViewHolder {
        return QnaItemView(context, parent)
    }

    /**
     */

    inner class QnaItemView(context: Context, parent: ViewGroup) :
            RecyclerItemView<Data>(context, LayoutInflater.from(context).inflate(R.layout.view_qna_item, parent, false)) {

        init {
            itemView?.run {
                imageView_profile.radius(context, R.drawable.vc_profile_white, 32)
            }
        }

        override fun refreshView(data: Data) = with(itemView) {
            data.run {
            }
        }
    }

    data class Data(
            val id: Int,
            val qna: Qna? = null
    ) : ItemData

}