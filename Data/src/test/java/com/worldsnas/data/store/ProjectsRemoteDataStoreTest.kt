package com.worldsnas.data.store

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.worldsnas.data.model.ProjectEntity
import com.worldsnas.data.repository.ProjectsRemote
import com.worldsnas.data.test.factory.DataFactory
import com.worldsnas.data.test.factory.ProjectFactory
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProjectsRemoteDataStoreTest {

    private val remote = mock<ProjectsRemote>()
    private val store = ProjectsRemoteDataStore(remote)

    @Test
    fun getProjectsReturnsValue(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubGetProject(Observable.just(projects))

        val observer = store.getProjects().test()

        observer.assertValue(projects)
    }


    @Test
    fun getProjectsCallsRemote(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubGetProject(Observable.just(projects))

        store.getProjects().test()

        verify(remote).getProjects()
    }

    @Test
    fun getProjectsComplete(){
        val projects = listOf(ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity(), ProjectFactory.makeProjectEntity())

        stubGetProject(Observable.just(projects))

        val observer = store.getProjects().test()

        observer.assertComplete()
    }

    @Test(expected = UnsupportedOperationException::class)
    fun clearProjectsThrows(){
        store.clearProjects()
    }

    @Test(expected = UnsupportedOperationException::class)
    fun saveProjectsThrows(){
        store.saveProjects(listOf(ProjectFactory.makeProjectEntity()))
    }

    @Test(expected = UnsupportedOperationException::class)
    fun getBookmarkedProjectsThrows(){
        store.getBookmarkProjects()
    }

    @Test(expected = UnsupportedOperationException::class)
    fun setBookmarkProjectsThrows(){
        store.setProjectAsBookmark(DataFactory.randomUuid())
    }

    @Test(expected = UnsupportedOperationException::class)
    fun setNotBookmarkProjectsThrows(){
        store.setProjectAsNotBookmark(DataFactory.randomUuid())
    }

    private fun stubGetProject(observable: Observable<List<ProjectEntity>>){
        whenever(remote.getProjects())
                .thenReturn(observable)
    }
}