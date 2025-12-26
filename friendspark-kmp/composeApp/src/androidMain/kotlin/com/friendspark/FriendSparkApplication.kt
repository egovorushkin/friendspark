package com.friendspark

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.friendspark.di.appModule

class FriendSparkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FriendSparkApplication)
            modules(appModule)
        }
    }
}

