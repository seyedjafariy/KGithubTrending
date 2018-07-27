package com.worldsnas.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import com.worldsnas.domain.interactor.bookmark.GetBookmarkedProjects
import com.worldsnas.domain.model.Project
import com.worldsnas.presentation.factory.DataFactory
import com.worldsnas.presentation.factory.ProjectFactory
import com.worldsnas.presentation.mapper.ProjectViewMapper
import com.worldsnas.presentation.model.ProjectView
import com.worldsnas.presentation.state.ResourceState
import io.reactivex.observers.DisposableObserver
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Captor

@RunWith(JUnit4::class)
class BrowseBookmarkedProjectViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val getBookmarkProject = mock<GetBookmarkedProjects>()
    val mapper = mock<ProjectViewMapper>()
    val viewModel = BrowseBookmarkedProjectViewModel(getBookmarkProject, mapper)

    @Captor
    val captor = argumentCaptor<DisposableObserver<List<Project>>>()

    @Test
    fun fetchProjectExecutesUseCase() {
        viewModel.fetchProjects()

        verify(getBookmarkProject, times(1)).execute(any(), eq(null))
    }

    @Test
    fun fetchProjectReturnsSuccess() {
        val projects = ProjectFactory.makeProjectList(2)
        val projectViews = ProjectFactory.makeProjectViewList(2)
        stubProjectMapperMapToView(projectViews[0], projects[0])
        stubProjectMapperMapToView(projectViews[1], projects[1])

        viewModel.fetchProjects()

        verify(getBookmarkProject, times(1)).execute(captor.capture(), eq(null))
        captor.firstValue.onNext(projects)

        assertEquals(ResourceState.SUCCESS, viewModel.getProjects().value?.status)
    }

    @Test
    fun fetchProjectReturnsData() {
        val projects = ProjectFactory.makeProjectList(2)
        val projectViews = ProjectFactory.makeProjectViewList(2)
        stubProjectMapperMapToView(projectViews[0], projects[0])
        stubProjectMapperMapToView(projectViews[1], projects[1])

        viewModel.fetchProjects()

        verify(getBookmarkProject, times(1)).execute(captor.capture(), eq(null))
        captor.firstValue.onNext(projects)

        assertEquals(projectViews, viewModel.getProjects().value?.data)
    }

    @Test
    fun fetchProjectReturnsError() {
        viewModel.fetchProjects()

        verify(getBookmarkProject, times(1)).execute(captor.capture(), eq(null))
        captor.firstValue.onError(RuntimeException())

        assertEquals(ResourceState.ERROR, viewModel.getProjects().value?.status)
    }

    @Test
    fun fetchProjectReturnsErrorMessage() {
        val errorMessage = DataFactory.randomUuid()

        viewModel.fetchProjects()

        verify(getBookmarkProject, times(1)).execute(captor.capture(), eq(null))
        captor.firstValue.onError(RuntimeException(errorMessage))

        assertEquals(errorMessage, viewModel.getProjects().value?.message)
    }

    private fun stubProjectMapperMapToView(projectView: ProjectView,
                                           project: Project){
        whenever(mapper.mapToView(project))
                .thenReturn(projectView)
    }

}