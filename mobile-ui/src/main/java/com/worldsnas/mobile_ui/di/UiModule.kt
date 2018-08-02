package com.worldsnas.mobile_ui.di

import com.worldsnas.domain.executor.PostExecutionThread
import com.worldsnas.mobile_ui.BrowseActivity
import com.worldsnas.mobile_ui.UiThread
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiModule {

    @Binds
    abstract fun bindPostExecution(uiThread: UiThread):PostExecutionThread

    @ContributesAndroidInjector
    abstract fun bindBrowseActivity():BrowseActivity
}