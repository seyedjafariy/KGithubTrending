package com.worldsnas.data.store

import com.worldsnas.data.model.ProjectEntity
import com.worldsnas.data.repository.ProjectsCache
import com.worldsnas.data.repository.ProjectsDataStore
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

open class ProjectCacheDataStore @Inject constructor(
        private val projectsCache: ProjectsCache) :
        ProjectsDataStore {
    override fun getProjects(): Observable<List<ProjectEntity>> {
        return projectsCache.getProjects()
    }

    override fun clearProjects(): Completable {
        return projectsCache.clearProjects()
    }

    override fun saveProjects(projects: List<ProjectEntity>): Completable {
        return projectsCache.saveProjects(projects)
                .andThen(projectsCache.setLastCacheTime(System.currentTimeMillis()))
    }

    override fun getBookmarkProjects(): Observable<List<ProjectEntity>> {
        return projectsCache.getBookmarkProjects()
    }

    override fun setProjectAsBookmark(projectId: String): Completable {
        return projectsCache.setProjectAsBootkmark(projectId)
    }

    override fun setProjectAsNotBookmark(projectId: String): Completable {
        return projectsCache.setProjectAsNotBootkmark(projectId)
    }
}