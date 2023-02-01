package com.like.hmlib

import android.app.Application
import com.like.kotlinkit.proxy.ApplicationProxy
import timber.log.Timber
import java.io.File


/**
 * @author like
 */
class MyApplication : Application() {

    companion object {
        fun get(): MyApplication {
            return ApplicationProxy.getApp() as MyApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationProxy.init(this)
        Timber.plant(Timber.DebugTree())
    }
}

    
