package com.worldsnas.data.store

import com.worldsnas.data.model.ProjectEntity
import com.worldsnas.data.repository.ProjectsDataStore
import com.worldsnas.data.repository.ProjectsRemote
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class ProjectsRemoteDataStore @Inject constructor(
        private val projectsRemote: ProjectsRemote) :
        ProjectsDataStore{
    override fun getProjects(): Observable<List<ProjectEntity>> {
        return projectsRemote.getProjects()
    }

    override fun clearProjects(): Completable {
        throw UnsupportedOperationException("Clearing Projects is not supported here")
    }

    override fun saveProjects(projects: List<ProjectEntity>): Completable {
        throw UnsupportedOperationException("Saving Projects is not supported here")
    }

    override fun getBookmarkProjects(): Observable<List<ProjectEntity>> {
        throw UnsupportedOperationException("Get Bookmark Projects is not supported here")
    }

    override fun setProjectAsBookmark(projectId: String): Completable {
        throw UnsupportedOperationException("Set Project As Bookmark is not supported here")
    }

    override fun setProjectAsNotBookmark(projectId: String): Completable {
        throw UnsupportedOperationException("Set Project As Not Bookmark is not supported here")
    }
}