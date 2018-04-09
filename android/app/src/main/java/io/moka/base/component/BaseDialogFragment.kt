package io.moka.base.component


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.moka.mokabaselib.util.log.MLog


abstract class BaseDialogFragment : AppCompatDialogFragment() {

    var onDismiss: (() -> Unit)? = null

    /**
     * Dialog LifeCycle
     * onCreate -> onCreateDialog -> onCreateView -> onViewCreated -> onResume
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        MLog.a("Dialog", "onCreateView called #####")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resizeFontSizeAndExt()
        MLog.a("Dialog", "onViewCreated called #####")
    }

    abstract fun resizeFontSizeAndExt()

    override fun onResume() {
        super.onResume()
    }

    override fun show(transaction: FragmentTransaction, tag: String): Int {
        return super.show(transaction, tag)
    }

    override fun show(manager: FragmentManager, tag: String) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commit()
        } catch (e: IllegalStateException) {
            MLog.deb("Ignore Can not perform this action after onSaveInstanceStateAsk Question")
        }

    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

}
