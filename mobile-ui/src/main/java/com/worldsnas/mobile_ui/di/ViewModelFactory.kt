package com.worldsnas.mobile_ui.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
open class ViewModelFactory : ViewModelProvider.Factory {

    private val creators: Map<Class<out ViewModel>, Provider<ViewModel>>

    @Inject
    constructor(creator: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) {
        creators = creator
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator : Provider<out ViewModel>? = creators[modelClass]
        if (creator == null){
            for((key, value) in creators){
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }

        if (creator == null){
            throw IllegalStateException("unknown model class: $modelClass")
        }
        return creator.get() as T
    }
}