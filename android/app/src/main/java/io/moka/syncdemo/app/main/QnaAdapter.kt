package io.moka.syncdemo.app.main

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.moka.base.module.radius
import io.moka.mokabaselib.adapter.BaseAdapter
import io.moka.mokabaselib.adapter.ItemData
import io.moka.mokabaselib.adapter.RecyclerItemView
import io.moka.mokabaselib.util.gone
import io.moka.mokabaselib.util.visible
import io.moka.syncdemo.R
import io.moka.syncdemo.model.domain.Answer
import io.moka.syncdemo.model.domain.Question
import kotlinx.android.synthetic.main.view_qna_item.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk15.coroutines.onClick

class QnaAdapter(private val context: Context) : BaseAdapter<QnaAdapter.Data, RecyclerItemView<QnaAdapter.Data>>() {

    var onClickItem: ((qna: Data) -> Unit)? = null

    override fun onCreateContentItemViewHolder(parent: ViewGroup, contentViewType: Int): RecyclerView.ViewHolder {
        return QnaItemView(context, parent)
    }

    /**
     */

    inner class QnaItemView(context: Context, parent: ViewGroup) :
            RecyclerItemView<Data>(context, LayoutInflater.from(context).inflate(R.layout.view_qna_item, parent, false)) {

        init {
            itemView?.run {
                imageView_profile.radius(context, R.drawable.vc_profile_white, 28)

                constraintLayout_container.onClick { onClickItem?.invoke(data) }
            }
        }

        override fun refreshView(data: Data) = with(itemView) {
            data.run {
                /* */
                if (null != answers && answers.size > 1) {
                    textView_answer_more.visible()
                    textView_answer_more.text = "${answers.size - 1}개의 답변이 더 있습니다"

                    textView_answer.alpha = 1f
                    textView_answer.text = answers.first().answer
                    textView_answer.typeface = Typeface.DEFAULT_BOLD
                }
                else if (null != answers && answers.size == 1) {
                    textView_answer_more.visible()
                    textView_answer_more.text = "총 ${answers.size}개의 답변이 있습니다"

                    textView_answer.alpha = 1f
                    textView_answer.text = answers.first().answer
                    textView_answer.typeface = Typeface.DEFAULT_BOLD
                }
                else {
                    textView_answer_more.gone()

                    textView_answer.alpha = 0.4f
                    textView_answer.text = "답변을 기다리고 있습니다"
                    textView_answer.typeface = Typeface.DEFAULT
                }

                /* */
                textView_question.text = question.question

                /* */
                if (question.name.isNullOrEmpty() and question.say.isNullOrEmpty()) {
                    imageView_profile.layoutParams.width = 0

                    textView_name.text = "-"
                    textView_say.text = "익명 입니다"
                }
                else {
                    imageView_profile.layoutParams.width = dip(32)

                    textView_name.text = question.name
                    textView_say.text = question.say
                }

                Unit
            }
        }
    }

    data class Data(
            val question: Question,
            val answers: List<Answer>? = null
    ) : ItemData

}