package com.worldsnas.mobile_ui.di

import android.content.Context
import com.worldsnas.mobile_ui.GithubTrendingApplication
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(githubTrendingApplication: GithubTrendingApplication): Context
}