package com.worldsnas.data

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.worldsnas.data.mapper.ProjectMapper
import com.worldsnas.data.model.ProjectEntity
import com.worldsnas.data.repository.ProjectsCache
import com.worldsnas.data.repository.ProjectsDataStore
import com.worldsnas.data.store.ProjectsDataStoreFactory
import com.worldsnas.data.test.factory.DataFactory

import com.worldsnas.data.test.factory.ProjectFactory
import com.worldsnas.domain.model.Project
import io.reactivex.Completable


import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProjectsDataRepositoryTest {

    private val factory = mock<ProjectsDataStoreFactory>()
    private val mapper = mock<ProjectMapper>()
    private val cache = mock<ProjectsCache>()
    private val store = mock<ProjectsDataStore>()
    private val repository = ProjectsDataRepository(mapper, cache, factory)

    @Before
    fun setUp() {
        stubIsCacheExpired(Single.just(false))
        stubIsProjectCache(Single.just(false))
        stubFactoryGetDatastore()
        stubFactoryGetCache()
        stubSaveProject(Completable.complete())
    }

    @Test
    fun getProjectsCompletes() {
        val projectEntities = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())
        stubGetProjects(Observable.just(projectEntities))
//        stubMapper(ProjectFactory.makeProject(), any())

        val testObs = repository.getProjects().test()

        testObs.assertComplete()
    }

    @Test
    fun getProjectsReturnsData() {
        val projectEntity = ProjectFactory.makeProjectEntity()
        val projectEntities = listOf(projectEntity)
        val project = ProjectFactory.makeProject()

        stubGetProjects(Observable.just(projectEntities))

        stubMapper(project, projectEntity)

        val testObs = repository.getProjects().test()

        testObs.assertValue(listOf(project))
    }

    @Test
    fun getBookmarkProjectsReturnsValue() {
        val projectEntity = ProjectFactory.makeProjectEntity()
        val projectEntities = listOf(projectEntity)
        val project = ProjectFactory.makeProject()

        stubBookmarkProject(Observable.just(projectEntities))

        stubMapper(project, projectEntity)

        val observer = repository.getBookmarkedProjects().test()

        observer.assertValue(listOf(project))
    }

    @Test
    fun getBookmarkProjectsCompletes() {
        val projectEntity = ProjectFactory.makeProjectEntity()
        val projectEntities = listOf(projectEntity)
        val project = ProjectFactory.makeProject()

        stubBookmarkProject(Observable.just(projectEntities))

        stubMapper(project, projectEntity)

        repository.getBookmarkedProjects().test().assertComplete()
    }

    @Test
    fun bookmarkProjectsCompletes() {
        stubSetProjectAsBookmark(Completable.complete())

        repository.bookmarkProject(DataFactory.randomUuid())
                .test()
                .assertComplete()
    }

    @Test
    fun unBookmarkProjectsCompletes() {
        stubSetProjectAsNotBookmark(Completable.complete())

        repository.unbookmarkProject(DataFactory.randomUuid())
                .test()
                .assertComplete()
    }

    private fun stubIsProjectCache(areProjectsCacheSingle: Single<Boolean>){
        whenever(cache.areProjectsCached())
                .thenReturn(areProjectsCacheSingle)
    }

    private fun stubIsCacheExpired(isExpiredSingle: Single<Boolean>){
        whenever(cache.isProjectCachExpired())
                .thenReturn(isExpiredSingle)
    }

    private fun stubMapper(model: Project, entity: ProjectEntity) {
        whenever(mapper.mapFromEntity(entity))
                .thenReturn(model)
    }

    private fun stubGetProjects(observable: Observable<List<ProjectEntity>>) {
        whenever(store.getProjects())
                .thenReturn(observable)
    }

    private fun stubFactoryGetDatastore() {
        whenever(factory.getDataStore(any(), any()))
                .thenReturn(store)
    }

    private fun stubFactoryGetCache() {
        whenever(factory.getCacheDataStore())
                .thenReturn(store)
    }

    private fun stubSaveProject(completable: Completable) {
        whenever(store.saveProjects(any()))
                .thenReturn(completable)
    }

    private fun stubBookmarkProject(observable: Observable<List<ProjectEntity>>) {
        whenever(factory.getCacheDataStore().getBookmarkProjects())
                .thenReturn(observable)
    }

    private fun stubSetProjectAsBookmark(completable: Completable){
        whenever(store.setProjectAsBookmark(any()))
                .thenReturn(completable)
    }

    private fun stubSetProjectAsNotBookmark(completable: Completable){
        whenever(store.setProjectAsNotBookmark(any()))
                .thenReturn(completable)
    }

}