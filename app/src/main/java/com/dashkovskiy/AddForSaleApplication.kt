package com.dashkovskiy

import android.app.Application
import com.dashkovskiy.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AddForSaleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AddForSaleApplication)
            modules(
                retrofitModule,
                viewModelsModule,
                repositoriesModule,
                preferencesModule,
                pagersModule
            )
        }
    }
}