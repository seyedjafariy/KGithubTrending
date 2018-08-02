package com.worldsnas.mobile_ui.di

import com.worldsnas.cache.ProjectsCacheImpl
import com.worldsnas.data.repository.ProjectsCache
import dagger.Binds
import dagger.Module

@Module
abstract class CacheModule {

    @Binds
    abstract fun bindProjectCache(projectsCacheImpl: ProjectsCacheImpl): ProjectsCache
}