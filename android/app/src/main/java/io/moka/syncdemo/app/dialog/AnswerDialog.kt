package io.moka.syncdemo.app.dialog


import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.moka.base.component.BaseDialogFragment
import io.moka.syncdemo.R
import kotlinx.android.synthetic.main.dialog_answer.*
import org.jetbrains.anko.sdk15.coroutines.onClick


class AnswerDialog : BaseDialogFragment() {

    var question: String? = null

    private var onClickOk: ((answer: String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_answer, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        textView_ok.onClick { onClickOk() }
        textView_cancel.onClick { dismiss() }
    }

    override fun onResume() {
        super.onResume()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     */

    private fun initView() {
        textView_question.text = question
    }

    /**
     */

    private fun onClickOk() {
        onClickOk?.invoke(editText_content.text.toString())
        dismiss()
    }

    /**
     */

    fun showDialog(manager: FragmentManager, onClickOk: (answer: String) -> Unit) {
        this.onClickOk = onClickOk
        show(manager, "AnswerDialog")
    }

}
