package io.moka.syncdemo.app.detail.dialog


import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.moka.base.component.BaseDialogFragment
import io.moka.syncdemo.R
import kotlinx.android.synthetic.main.dialog_post_question.*
import org.jetbrains.anko.sdk15.coroutines.onClick


class PostAnswerDialog : BaseDialogFragment() {

    private var onClickOk: ((text: String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_post_answer, null)
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

    }

    /**
     */

    private fun onClickOk() {
        onClickOk?.invoke(editText_name.text.toString())
        dismiss()
    }

    /**
     */

    fun showDialog(manager: FragmentManager, onClickOk: (text: String) -> Unit) {
        this.onClickOk = onClickOk
        show(manager, "PostAnswerDialog")
    }

}
