package com.friendspark

import android.app.Application
import com.friendspark.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FriendSparkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FriendSparkApplication)
            modules(appModule)
        }
    }
}

