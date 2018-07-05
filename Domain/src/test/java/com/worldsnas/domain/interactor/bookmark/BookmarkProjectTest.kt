package com.worldsnas.domain.interactor.bookmark

import com.nhaarman.mockito_kotlin.whenever
import com.worldsnas.domain.executor.PostExecutionThread
import com.worldsnas.domain.repository.ProjectRepository
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BookmarkProjectTest {

    private lateinit var bookmarkProject: BookmarkProject
    @Mock
    lateinit var projectRepository: ProjectRepository
    @Mock
    lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        bookmarkProject = BookmarkProject(projectRepository, postExecutionThread)

    }

    @Test
    fun getBookmarkProjectsCompletes() {
        val params = BookmarkProject.Params.forProject("")
        stubBookmarkProject(params.projectId)
        val testObserver = bookmarkProject.buildUseCaseCompletable(params).test()
        testObserver.assertComplete()
    }

    private fun stubBookmarkProject(projectId:String){
        whenever(projectRepository.bookmarkProject(projectId))
                .thenReturn(Completable.complete())
    }

}