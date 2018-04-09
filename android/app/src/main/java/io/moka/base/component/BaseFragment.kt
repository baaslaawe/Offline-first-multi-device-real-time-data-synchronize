package io.moka.base.component

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    /**
     * Lifecycle functions
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    fun onViewCreated(view: View, savedInstanceState: Bundle?, preventEventToBelow: Boolean) {
        super.onViewCreated(view, savedInstanceState)
        if (preventEventToBelow)
            view.setOnTouchListener({ _, _ -> true })

        onViewCreated()
    }

    private fun onViewCreated() {
        resizeFontSizeAndExt()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     */

    abstract fun resizeFontSizeAndExt()

}
