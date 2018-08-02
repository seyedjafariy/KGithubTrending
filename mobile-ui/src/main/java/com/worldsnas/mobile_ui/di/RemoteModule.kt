package com.worldsnas.mobile_ui.di

import com.worldsnas.data.repository.ProjectsRemote
import com.worldsnas.mobile_ui.BuildConfig
import com.worldsnas.remote.ProjectsRemoteImpl
import com.worldsnas.remote.service.GithubTrendingService
import com.worldsnas.remote.service.GithubTrendingServiceFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RemoteModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideGithubService(): GithubTrendingService {
            return GithubTrendingServiceFactory.makeGithubTrendingService(BuildConfig.DEBUG)
        }
    }

    @Binds
    abstract fun bindProjectsRemote(projectsRemoteImpl: ProjectsRemoteImpl): ProjectsRemote
}