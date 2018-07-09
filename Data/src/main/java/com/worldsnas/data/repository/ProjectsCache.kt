package com.worldsnas.data.repository

import com.worldsnas.data.model.ProjectEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ProjectsCache {

    fun clearProjects(): Completable

    fun saveProjects(projects: List<ProjectEntity>): Completable

    fun getProjects(): Observable<List<ProjectEntity>>

    fun getBookmarkProjects(): Observable<List<ProjectEntity>>

    fun setProjectAsBookmark(projectId: String): Completable

    fun setProjectAsNotBootkmark(projectId: String): Completable

    fun areProjectsCached(): Single<Boolean>

    fun setLastCacheTime(lastCache: Long): Completable

    fun isProjectCacheExpired(): Single<Boolean>
}