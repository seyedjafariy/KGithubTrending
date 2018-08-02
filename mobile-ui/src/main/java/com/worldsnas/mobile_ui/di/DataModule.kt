package com.worldsnas.mobile_ui.di

import com.worldsnas.data.ProjectsDataRepository
import com.worldsnas.domain.repository.ProjectRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindDataRepository(dataRepository: ProjectsDataRepository): ProjectRepository
}