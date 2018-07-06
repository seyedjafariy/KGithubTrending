package com.worldsnas.data

import com.worldsnas.data.mapper.ProjectMapper
import com.worldsnas.data.repository.ProjectsCache
import com.worldsnas.data.store.ProjectsDataStoreFactory
import com.worldsnas.domain.model.Project
import com.worldsnas.domain.repository.ProjectRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ProjectsDataRepository @Inject constructor(
        private val projectMapper: ProjectMapper,
        private val cache: ProjectsCache,
        private val factory: ProjectsDataStoreFactory) :
        ProjectRepository {
    override fun getProjects(): Observable<List<Project>> {
        return Observable.zip(cache.areProjectsCached().toObservable(),
                cache.isProjectCacheExpired().toObservable(),
                BiFunction<Boolean, Boolean, Pair<Boolean, Boolean>> { areCached, cacheExpired ->
                    Pair(areCached, cacheExpired)
                })
                .flatMap {
                    factory.getDataStore(it.first, it.second).getProjects()
                }
                .flatMap { projects ->
                    factory.getCacheDataStore().saveProjects(projects)
                            .andThen(Observable.just(projects))
                }
                .map {
                    it.map {
                        projectMapper.mapFromEntity(it)
                    }
                }
    }

    override fun bookmarkProject(projectId: String): Completable {
        return factory.getCacheDataStore().setProjectAsBookmark(projectId)
    }

    override fun unbookmarkProject(projectId: String): Completable {
        return factory.getCacheDataStore().setProjectAsNotBookmark(projectId)
    }

    override fun getBookmarkedProjects(): Observable<List<Project>> {
        return factory.getCacheDataStore().getBookmarkProjects()
                .map {
                    it.map {
                        projectMapper.mapFromEntity(it)
                    }
                }
    }
}