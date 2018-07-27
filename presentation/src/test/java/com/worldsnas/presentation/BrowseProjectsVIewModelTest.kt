package com.worldsnas.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import com.worldsnas.domain.interactor.bookmark.BookmarkProject
import com.worldsnas.domain.interactor.bookmark.UnbookmarkProject
import com.worldsnas.domain.interactor.browse.GetProjects
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
class BrowseProjectsVIewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val getProjects = mock<GetProjects>()
    val bookmarkProject = mock<BookmarkProject>()
    val unBookmarkProject = mock<UnbookmarkProject>()
    val mapper = mock<ProjectViewMapper>()
    val projectViewModel = BrowseProjectsVIewModel(getProjects, bookmarkProject, unBookmarkProject, mapper)

    @Captor
    val captor = argumentCaptor<DisposableObserver<List<Project>>>()

    @Test
    fun fetchProjectExecutesUseCase() {
        projectViewModel.fetchProjects()

        verify(getProjects, times(1)).execute(any(), eq(null))
    }

    @Test
    fun fetchProjectReturnsSuccess() {
        val projects = ProjectFactory.makeProjectList(2)
        val projectViews = ProjectFactory.makeProjectViewList(2)
        stubProjectMapperMapToView(projectViews[0], projects[0])
        stubProjectMapperMapToView(projectViews[1], projects[1])

        projectViewModel.fetchProjects()

        verify(getProjects).execute(captor.capture(), eq(null))
        captor.firstValue.onNext(projects)

        assertEquals(ResourceState.SUCCESS, projectViewModel.getProjects().value?.status)
    }

    @Test
    fun fetchProjectReturnsData() {
        val projects = ProjectFactory.makeProjectList(2)
        val projectViews = ProjectFactory.makeProjectViewList(2)
        stubProjectMapperMapToView(projectViews[0], projects[0])
        stubProjectMapperMapToView(projectViews[1], projects[1])

        projectViewModel.fetchProjects()

        verify(getProjects).execute(captor.capture(), eq(null))
        captor.firstValue.onNext(projects)

        assertEquals(ResourceState.SUCCESS, projectViewModel.getProjects().value?.data)
    }

    @Test
    fun fetchProjectReturnsError() {
        projectViewModel.fetchProjects()

        verify(getProjects).execute(captor.capture(), eq(null))
        captor.firstValue.onError(RuntimeException())

        assertEquals(ResourceState.ERROR, projectViewModel.getProjects().value?.status)
    }

    @Test
    fun fetchProjectReturnsMessageForError() {
        val errorMessage = DataFactory.randomUuid()

        projectViewModel.fetchProjects()

        verify(getProjects).execute(captor.capture(), eq(null))
        captor.firstValue.onError(RuntimeException(errorMessage))

        assertEquals(errorMessage, projectViewModel.getProjects().value?.message)
    }

    private fun stubProjectMapperMapToView(projectView: ProjectView,
                                           project: Project){
        whenever(mapper.mapToView(project))
                .thenReturn(projectView)
    }
}





















