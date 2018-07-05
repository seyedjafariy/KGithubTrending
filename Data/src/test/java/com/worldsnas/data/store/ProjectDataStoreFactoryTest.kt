package com.worldsnas.data.store

import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import kotlin.test.assertEquals

class ProjectDataStoreFactoryTest {

    private val cacheStore = mock<ProjectCacheDataStore>()
    private val remoteStore = mock<ProjectsRemoteDataStore>()
    private val factory = ProjectsDataStoreFactory(cacheStore, remoteStore)

    @Test
    fun getDataStoreReturnsRemoteWhenCacheExpired() {
        assertEquals(remoteStore, factory.getDataStore(true, true))
    }
}