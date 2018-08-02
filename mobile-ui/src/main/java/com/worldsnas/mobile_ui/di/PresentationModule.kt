package com.worldsnas.mobile_ui.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.worldsnas.presentation.BrowseProjectsVIewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PresentationModule {

    @Binds
    @IntoMap
    @ViewModelKey(BrowseProjectsVIewModel::class)
    abstract fun bindBrowseViewModel(browseProjectsVIewModel: BrowseProjectsVIewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}

