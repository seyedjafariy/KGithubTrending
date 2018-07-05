package com.worldsnas.domain.interactor.bookmark

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.worldsnas.domain.executor.PostExecutionThread
import com.worldsnas.domain.repository.ProjectRepository
import com.worldsnas.domain.test.ProjectDataFactory
import io.reactivex.Completable
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UnbookmarkProjectTest {

    private lateinit var unbookmarkProject: UnbookmarkProject
    @Mock lateinit var projectRepository : ProjectRepository
    @Mock lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        unbookmarkProject = UnbookmarkProject(projectRepository, postExecutionThread)
    }

    @Test
    fun unBookmarkCompletes() {
        val projectId = UnbookmarkProject.Params.forProject(ProjectDataFactory.randomUuid())
        stubUnBookmarkProject(Completable.complete())

        val completable = unbookmarkProject.buildUseCaseCompletable(projectId).test()
        completable.assertComplete()
    }

    @Test(expected = IllegalArgumentException::class)
    fun unBookmarkThrowsException(){
        unbookmarkProject.buildUseCaseCompletable().test()
    }

    private fun stubUnBookmarkProject(completable: Completable){
        whenever(projectRepository.unbookmarkProject(any()))
                .thenReturn(completable)
    }
}