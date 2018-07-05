package com.worldsnas.data.repository

import com.worldsnas.data.model.ProjectEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface ProjectsDataStore {

    fun getProjects(): Observable<List<ProjectEntity>>

    fun clearProjects(): Completable

    fun saveProjects(projects : List<ProjectEntity>): Completable

    fun getBookmarkProjects(): Observable<List<ProjectEntity>>

    fun setProjectAsBookmark(projectId: String): Completable

    fun setProjectAsNotBookmark(projectId: String): Completable

}