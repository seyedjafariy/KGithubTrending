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

    @Test
    fun getDataStoreReturnsRemoteWhenNoProjectCached() {
        assertEquals(remoteStore, factory.getDataStore(false, false))
        assertEquals(remoteStore, factory.getDataStore(false, true))
    }

    @Test
    fun getDataStoreReturnsCacheWhenNotExpired() {
        assertEquals(cacheStore, factory.getDataStore(true, false))
    }

    @Test
    fun getDataStoreReturnsCache() {
        assertEquals(cacheStore, factory.getCacheDataStore())
    }
}