package com.worldsnas.data.repository

import com.worldsnas.data.model.ProjectEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface ProjectsDataStore {

    fun getProjects(): Observable<List<ProjectEntity>>

    fun getBookmarkProjects(): Observable<List<ProjectEntity>>

    fun setProjectAsBootkmark(projectId: String): Completable

    fun setProjectAsNotBootkmark(projectId: String): Completable

}