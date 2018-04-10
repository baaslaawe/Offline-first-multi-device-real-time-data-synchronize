package io.moka.dayday_alrm.model.dao

import io.moka.dayday_alrm.model.domain._BaseDomain
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class Observer<T : _BaseDomain> {

    private var subject: PublishSubject<Data<T>> = PublishSubject.create<Data<T>>()

    fun onInsert(model: T) {
        val data = Data(model)
        data.isCreated = true

        subject.onNext(data)
    }

    fun onUpdate(model: T) {
        val data = Data(model)
        data.isUpdated = true

        subject.onNext(data)
    }

    fun onDelete(model: T) {
        val data = Data(model)
        data.isDeleted = true

        subject.onNext(data)
    }

    fun setOnChange(callback: (model: Data<T>) -> Unit, initModel: T? = null): Disposable {
        val subscription = subject
                .retry(1)
                .subscribe(
                        { model -> callback(model) },
                        { it.printStackTrace() },
                        {})

        if (null != initModel) {
            val data = Data(initModel)
            data.isUpdated = true
            callback(data)
        }

        return subscription
    }

    fun setOnChangeObservable(): Observable<Data<T>> {
        return subject
    }

    data class Data<out T : _BaseDomain>(val data: T) {
        var isCreated: Boolean = false
        var isUpdated: Boolean = false
        var isDeleted: Boolean = false
    }

}