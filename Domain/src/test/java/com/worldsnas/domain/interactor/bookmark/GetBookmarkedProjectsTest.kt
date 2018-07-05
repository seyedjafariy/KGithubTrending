package com.worldsnas.domain.interactor.bookmark

import com.nhaarman.mockito_kotlin.whenever
import com.worldsnas.domain.executor.PostExecutionThread
import com.worldsnas.domain.model.Project
import com.worldsnas.domain.repository.ProjectRepository
import com.worldsnas.domain.test.ProjectDataFactory
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GetBookmarkedProjectsTest {
    private lateinit var getBookmarkedProjects: GetBookmarkedProjects
    @Mock
    lateinit var projectRepository: ProjectRepository
    @Mock
    lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getBookmarkedProjects = GetBookmarkedProjects(projectRepository, postExecutionThread)
    }

    @Test
    fun getBookmarkProjectsCompletes() {
        stubGetProjects(Observable.just(ProjectDataFactory.makeProjectList(2)))
        val testObservable = getBookmarkedProjects.buildUseCaseObservable().test()
        testObservable.assertComplete()
    }


    @Test
    fun getBookmarkReturnsData() {
        val data = ProjectDataFactory.makeProjectList(2)
        stubGetProjects(Observable.just(data))
        val testObservable = getBookmarkedProjects.buildUseCaseObservable().test()
        testObservable.assertValue(data)
    }

    private fun stubGetProjects(observable: Observable<List<Project>>) {
        whenever(projectRepository.getBookmarkedProjects())
                .thenReturn(observable)
    }
}