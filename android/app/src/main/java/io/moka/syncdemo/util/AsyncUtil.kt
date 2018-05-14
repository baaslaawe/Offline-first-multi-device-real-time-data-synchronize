package io.moka.syncdemo.util

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

fun postMainDelay(work: () -> Unit, delayMillis: Long = 0L, filter: (() -> Boolean)? = null) {
    async(CommonPool) {
        delay(delayMillis)
        launch(UI) {
            if (filter == null)
                work()
            else {
                if (filter())
                    work()
            }
        }

        Unit
    }
}

inline fun postMain(crossinline work: () -> Unit) {
    launch(UI) { work() }
}

inline fun postMain(crossinline work: () -> Unit, crossinline filter: (() -> Boolean)) {
    launch(UI) {
        if (filter())
            work()
    }
}

/*
todo : 프레그먼트가 destroy 될때 clear 하는 등이 필요 없는지 알아보기
 */
fun postDelay(work: () -> Unit, delayMillis: Long = 0L) {
    async(CommonPool) {
        delay(delayMillis)
        work()
        Unit
    }
}

fun workInBack(work: () -> Unit) {
    async(CommonPool) {
        work()
    }
}

fun paraller(work1: () -> Unit, work2: () -> Unit, callback: () -> Unit) = launch(UI) {
    val async1 = async(CommonPool) { work1() }
    val async2 = async(CommonPool) { work2() }

    async1.await()
    async2.await()

    callback()
}
