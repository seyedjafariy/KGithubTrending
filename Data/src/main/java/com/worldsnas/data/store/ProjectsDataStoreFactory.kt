package com.worldsnas.data.store

import com.worldsnas.data.repository.ProjectsDataStore
import javax.inject.Inject

open class ProjectsDataStoreFactory @Inject constructor(
        private val projectCacheDataStore: ProjectCacheDataStore,
        private val projectsRemoteDataStore: ProjectsRemoteDataStore) {

    open fun getDataStore(projectsCached: Boolean, cacheExpired: Boolean): ProjectsDataStore {
        return if (projectsCached && !cacheExpired) {
            projectCacheDataStore
        } else {
            projectsRemoteDataStore
        }
    }

    open fun getCacheDataStore(): ProjectsDataStore {
        return projectCacheDataStore
    }
}