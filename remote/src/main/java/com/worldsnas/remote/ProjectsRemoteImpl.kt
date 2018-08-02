package com.worldsnas.remote

import com.worldsnas.remote.mapper.ProjectsResponseModelMapper
import com.worldsnas.remote.service.GithubTrendingService
import com.worldsnas.data.model.ProjectEntity
import com.worldsnas.data.repository.ProjectsRemote
import io.reactivex.Observable
import javax.inject.Inject

open class ProjectsRemoteImpl @Inject constructor(
        private val service: GithubTrendingService,
        private val mapper: ProjectsResponseModelMapper)
    : ProjectsRemote {

    override fun getProjects(): Observable<List<ProjectEntity>> {
        return service.searchRepositories("language:kotlin", "stars", "desc")
                .map {
                    it.items.map { mapper.mapFromModel(it) }
                }
    }
}