package com.worldsnas.data.store

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.worldsnas.data.model.ProjectEntity
import com.worldsnas.data.repository.ProjectsCache
import com.worldsnas.data.test.factory.DataFactory
import com.worldsnas.data.test.factory.ProjectFactory
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class ProjectCacheDataStoreTest {

    @Mock
    lateinit var cache: ProjectsCache
    lateinit var store: ProjectCacheDataStore

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        store = ProjectCacheDataStore(cache)
    }

    @Test
    fun returnsCompletableWhenClearingProjects(){
        val completable = Completable.complete()
        stubCache(completable)

        assertEquals(completable, store.clearProjects())
    }

    @Test
    fun completesCompletableWhenClearingProjects(){
        val completable = Completable.complete()
        stubCache(completable)

        val testCompletable = store.clearProjects().test()

        testCompletable.assertComplete()
    }

    @Test
    fun clearProjectsCallsCacheClear(){
        val completable = Completable.complete()
        stubCache(completable)

        store.clearProjects().test()

        verify(cache).clearProjects()
    }

    @Test
    fun getProjectsCompletes(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubProjectsCacheGetProjects(Observable.just(projects))

        store.getProjects().test().assertComplete()
    }

    @Test
    fun getProjectsEmitTheSameValue(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubProjectsCacheGetProjects(Observable.just(projects))

        store.getProjects().test().assertValue(projects)
    }

    @Test
    fun getProjectsCallsCacheStore(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubProjectsCacheGetProjects(Observable.just(projects))

        store.getProjects().test()

        verify(cache).getProjects()
    }

    @Test
    fun saveProjectsCompletes(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubLastCacheTime(Completable.complete())
        stubSaveProjects(Completable.complete())

        val testCompletable = store.saveProjects(projects).test()

        testCompletable.assertComplete()
    }

    @Test
    fun saveProjectsCallsCacheSaveProjects(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubLastCacheTime(Completable.complete())
        stubSaveProjects(Completable.complete())

        store.saveProjects(projects).test()

        verify(cache).saveProjects(projects)
    }

    @Test
    fun saveProjectsCallsCacheSetLastCacheTime(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubLastCacheTime(Completable.complete())
        stubSaveProjects(Completable.complete())

        store.saveProjects(projects).test()

        verify(cache).setLastCacheTime(any())
    }

    @Test
    fun getBookmarkProjectsReturnsValue(){

        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubGetBookmarkProjects(Observable.just(projects))

        val testObservable = store.getBookmarkProjects().test()

        testObservable.assertValue(projects)
    }

    @Test
    fun getBookmarkProjectsCompletes(){

        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubGetBookmarkProjects(Observable.just(projects))

        val testObservable = store.getBookmarkProjects().test()

        testObservable.assertComplete()
    }

    @Test
    fun getBookmarkCallsCacheGetBookmark(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubGetBookmarkProjects(Observable.just(projects))

        store.getBookmarkProjects().test()

        verify(cache).getBookmarkProjects()
    }

    @Test
    fun setBookmarkCompletes(){
        val projectId = DataFactory.randomUuid()

        stubSetProjectAsBookmarked(Completable.complete())

        val testObservable = store.setProjectAsBookmark(projectId).test()

        testObservable.assertComplete()
    }

    @Test
    fun setBookmarkCallsCacheStore(){
        val projectId = DataFactory.randomUuid()

        stubSetProjectAsBookmarked(Completable.complete())

        store.setProjectAsBookmark(projectId).test()

        verify(cache).setProjectAsBootkmark(projectId)
    }

    @Test
    fun setNotBookmarkCompletes(){
        val projectId = DataFactory.randomUuid()

        stubSetProjectAsNotBookmarked(Completable.complete())

        val testObservable = store.setProjectAsNotBookmark(projectId).test()

        testObservable.assertComplete()
    }

    @Test
    fun setNotBookmarkCallsCacheStore(){
        val projectId = DataFactory.randomUuid()

        stubSetProjectAsNotBookmarked(Completable.complete())

        store.setProjectAsNotBookmark(projectId).test()

        verify(cache).setProjectAsNotBootkmark(projectId)
    }

    private fun stubSetProjectAsNotBookmarked(completable: Completable){
        whenever(cache.setProjectAsNotBootkmark(any()))
                .thenReturn(completable)
    }

    private fun stubSetProjectAsBookmarked(completable: Completable){
        whenever(cache.setProjectAsBootkmark(any()))
                .thenReturn(completable)
    }

    private fun stubGetBookmarkProjects(observable: Observable<List<ProjectEntity>>){
        whenever(cache.getBookmarkProjects())
                .thenReturn(observable)
    }

    private fun stubCache(completable: Completable){
        whenever(cache.clearProjects())
                .thenReturn(completable)
    }

    private fun stubSaveProjects(completable: Completable){
        whenever(cache.saveProjects(any()))
                .thenReturn(completable)
    }

    private fun stubLastCacheTime(completable: Completable){
        whenever(cache.setLastCacheTime(any()))
                .thenReturn(completable)
    }
    
    private fun stubProjectsCacheGetProjects(observable: Observable<List<ProjectEntity>>){
        whenever(cache.getProjects())
                .thenReturn(observable)
    }
}























