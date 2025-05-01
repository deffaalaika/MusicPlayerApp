package com.deffa.musicplayerapp

import android.app.Application
import com.deffa.musicplayerapp.di.networkModule
import com.deffa.musicplayerapp.di.repositoryModule
import com.deffa.musicplayerapp.di.useCaseModule
import com.deffa.musicplayerapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MusicPlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicPlayerApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}