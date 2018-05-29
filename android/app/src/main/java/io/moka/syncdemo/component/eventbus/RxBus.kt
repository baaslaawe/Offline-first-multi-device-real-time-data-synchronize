package io.moka.syncdemo.component.eventbus

import android.support.annotation.IntDef
import android.util.SparseArray
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.*

object RxBus {

    private val sSubjectMap = SparseArray<PublishSubject<Any>>()
    private val sSubscriptionsMap = HashMap<Any, CompositeDisposable>()

    @IntDef()
    private annotation class Subject

    /**
     * Get the subject or create it if it's not already in memory.
     */
    private fun getSubject(@Subject subjectCode: Int): PublishSubject<Any> {
        var subject: PublishSubject<Any>? = sSubjectMap.get(subjectCode)
        if (subject == null) {
            subject = PublishSubject.create()
            subject!!.subscribeOn(AndroidSchedulers.mainThread())
            sSubjectMap.put(subjectCode, subject)
        }

        return subject
    }

    /**
     * Get the CompositeDisposable or create it if it's not already in memory.
     */
    private fun getCompositeDisposable(`object`: Any): CompositeDisposable {
        var compositeDisposable: CompositeDisposable? = sSubscriptionsMap[`object`]
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
            sSubscriptionsMap[`object`] = compositeDisposable
        }

        return compositeDisposable
    }

    /**
     * Subscribe to the specified subject and listen for updates on that subject. Pass in an object to associate
     * your registration with, so that you can unsubscribe later.
     * <br></br><br></br>
     * **Note:** Make sure to call [RxBus.unregister] to avoid memory leaks.
     */
    fun subscribe(@Subject subject: Int, lifecycle: Any, action: (Any) -> Unit) {
        val disposable = getSubject(subject).subscribe(action)
        getCompositeDisposable(lifecycle).add(disposable)
    }

    /**
     * Unregisters this object from the bus, removing all subscriptions.
     * This should be called when the object is going to go out of memory.
     */
    fun unregister(lifecycle: Any) {
        //We have to remove the composition from the map, because once you dispose it can't be used anymore
        val compositeDisposable = sSubscriptionsMap.remove(lifecycle)
        compositeDisposable?.dispose()
    }

    /**
     * Publish an object to the specified subject for all subscribers of that subject.
     */
    fun publish(@Subject subject: Int, message: Any = Any()) {
        getSubject(subject).onNext(message)
    }

}