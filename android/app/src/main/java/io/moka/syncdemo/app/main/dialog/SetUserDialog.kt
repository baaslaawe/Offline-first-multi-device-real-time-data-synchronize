package io.moka.syncdemo.app.main.dialog


import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.moka.base.component.BaseDialogFragment
import io.moka.syncdemo.R
import kotlinx.android.synthetic.main.dialog_set_user.*
import org.jetbrains.anko.sdk15.coroutines.onClick


class SetUserDialog : BaseDialogFragment() {

    var name: String? = null
    var say: String? = null

    private var onClickOk: ((name: String, say: String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_set_user, null)
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
        editText_name.setText(name)
        editText_say.setText(say)
    }

    /**
     */

    private fun onClickOk() {
        onClickOk?.invoke(editText_name.text.toString(), editText_say.text.toString())
        dismiss()
    }

    /**
     */

    fun showDialog(manager: FragmentManager, onClickOk: (name: String, say: String) -> Unit) {
        this.onClickOk = onClickOk
        show(manager, "SetUserDialog")
    }

}
