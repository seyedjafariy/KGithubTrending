package com.worldsnas.mobile_ui.di

import android.app.Application
import android.app.Presentation
import com.worldsnas.mobile_ui.GithubTrendingApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules= arrayOf(AndroidSupportInjectionModule::class,
        AppModule::class,
        UiModule::class,
        PresentationModule::class,
        DataModule::class,
        CacheModule::class,
        RemoteModule::class))
interface ApplicationComponent : AndroidInjector<GithubTrendingApplication>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder

        fun build(): ApplicationComponent
    }
}