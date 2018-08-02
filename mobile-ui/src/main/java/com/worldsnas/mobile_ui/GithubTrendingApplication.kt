package com.worldsnas.mobile_ui

import com.worldsnas.mobile_ui.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class GithubTrendingApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)
        return component
    }

    override fun onCreate() {
        super.onCreate()

        setupTimber()

//        DaggerApplicationComponent
//                .Builder()
//                .application(this)
//                .build()
//                .inject(this)
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}