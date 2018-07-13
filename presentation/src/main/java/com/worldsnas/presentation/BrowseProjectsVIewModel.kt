package com.worldsnas.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.worldsnas.domain.interactor.bookmark.BookmarkProject
import com.worldsnas.domain.interactor.bookmark.UnbookmarkProject
import com.worldsnas.domain.interactor.browse.GetProjects
import com.worldsnas.domain.model.Project
import com.worldsnas.presentation.mapper.ProjectViewMapper
import com.worldsnas.presentation.model.ProjectView
import com.worldsnas.presentation.state.Resource
import com.worldsnas.presentation.state.ResourceState
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class BrowseProjectsVIewModel @Inject constructor(
        private val getProjects: GetProjects,
        private val bookmarkProject: BookmarkProject,
        private val unbookmarkProject: UnbookmarkProject,
        private val mapper: ProjectViewMapper) : ViewModel() {

    private val liveData: MutableLiveData<Resource<List<ProjectView>>> = MutableLiveData()

    override fun onCleared() {
        getProjects.dispose()
        super.onCleared()
    }

    fun getProjects(): LiveData<Resource<List<ProjectView>>> = liveData

    fun fetchProjects() {
        liveData.postValue(Resource(ResourceState.LOADING, null, null))
        return getProjects.execute(ProjectSubscriber())
    }

    fun bookmarkProject(projectId: String) {
        liveData.postValue(Resource(ResourceState.LOADING, null, null))
        return bookmarkProject.execute(BookmarkProjectsSubscriber(),
                BookmarkProject.Params.forProject(projectId))
    }

    fun unBookmarkProject(projectId: String) {
        liveData.postValue(Resource(ResourceState.LOADING, null, null))
        return unbookmarkProject.execute(BookmarkProjectsSubscriber(),
                UnbookmarkProject.Params.forProject(projectId))
    }

    inner class ProjectSubscriber : DisposableObserver<List<Project>>() {
        override fun onComplete() {
        }

        override fun onNext(t: List<Project>) {
            liveData.postValue(Resource(ResourceState.SUCCESS,
                    t.map { mapper.mapToView(it) },
                    null))
        }

        override fun onError(e: Throwable) {
            liveData.postValue(Resource(ResourceState.ERROR, null, e.localizedMessage))
        }

    }

    inner class BookmarkProjectsSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            liveData.postValue(Resource(ResourceState.SUCCESS,
                    liveData.value?.data, null))
        }

        override fun onError(e: Throwable) {
            liveData.postValue(Resource(ResourceState.ERROR, null, e.localizedMessage))
        }

    }
}