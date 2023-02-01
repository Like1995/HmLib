package com.like.kotlinkit

import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

object Rx {

    /**
     * IO操作
     */
    @JvmStatic
    fun <T> doIO(action: (ObservableEmitter<T>) -> Unit, observeOnMain: Boolean = true): Observable<T> {
        // onNext事件在主线程处理
        val observeThread: Scheduler = when (observeOnMain) {
            true -> AndroidSchedulers.mainThread()
            false -> Schedulers.io()
        }
        return Observable.create<T> { action(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(observeThread)
    }

    /**
     * computation操作
     */
    fun <T> doComputation(
        action: (ObservableEmitter<T>) -> Unit,
        observeOnMain: Boolean = true,
    ): Observable<T> {
        // onNext事件在主线程处理
        val observeThread: Scheduler = when (observeOnMain) {
            true -> AndroidSchedulers.mainThread()
            false -> Schedulers.computation()
        }
        return Observable.create<T> { action(it) }
            .subscribeOn(Schedulers.computation())
            .observeOn(observeThread)
    }

    /**
     * 新的线程操作
     */
    fun <T> doThread(
        action: (ObservableEmitter<T>) -> Unit,
        observeOnMain: Boolean = true,
    ): Observable<T> {
        // onNext事件在主线程处理
        val observeThread = when (observeOnMain) {
            true -> AndroidSchedulers.mainThread()
            false -> Schedulers.newThread()
        }
        return Observable.create<T> { action(it) }
            .subscribeOn(Schedulers.newThread())
            .observeOn(observeThread)
    }

    /**
     * 新的线程操作
     */
    fun <T> doClick(
        action: (ObservableEmitter<T>) -> Unit,
        observeOnMain: Boolean = true,
    ): Observable<Long> {
        // onNext事件在主线程处理
        val observeThread = when (observeOnMain) {
            true -> AndroidSchedulers.mainThread()
            false -> Schedulers.newThread()
        }
        return Observable.timer(600, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(observeThread)
    }


    /***
     * 设置延迟时间的View扩展
     * @param delay Long 延迟时间，默认600毫秒
     * @return T
     */
    fun <T : View> T.withTrigger(delay: Long = 600): T {
        triggerDelay = delay
        return this
    }

    /***
     * 点击事件的View扩展
     * @param block: (T) -> Unit 函数
     * @return Unit
     */
    fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {

        if (clickEnable()) {
            block(it as T)
        }
    }

    fun View.onDoubleClick(intervalTime: Long = 300, singleBlock: ((View) -> Unit)?, doubleBlock: ((View) -> Unit)?) {
        var lastTime = 0L
        this.setOnClickListener {
            val curTime = System.currentTimeMillis()
            if (curTime - lastTime < intervalTime) {
                doubleBlock?.invoke(this)
                lastTime = 0L
                Timber.v("$this-----双击")
            } else {
                singleBlock?.invoke(this)
                Timber.v("$this-----单机")
                lastTime = curTime
            }
        }
    }

    /***
     * 带延迟过滤的点击事件View扩展
     * @param delay Long 延迟时间，默认600毫秒
     * @param block: (T) -> Unit 函数
     * @return Unit
     */
    fun <T : View> T.clickWithTrigger(time: Long = 300, block: (T) -> Unit) {
        triggerDelay = time
        setOnClickListener {
            if (clickEnable()) {
                block(it as T)
            }
        }
    }

    private var <T : View> T.triggerLastTime: Long
        get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
        set(value) {
            setTag(1123460103, value)
        }

    private var <T : View> T.triggerDelay: Long
        get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
        set(value) {
            setTag(1123461123, value)
        }


    private fun <T : View> T.clickEnable(): Boolean {
        var flag = false
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - triggerLastTime >= triggerDelay) {
            flag = true
        }
        triggerLastTime = currentClickTime
        return flag
    }


}