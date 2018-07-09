package com.worldsnas.cache

import com.worldsnas.cache.mapper.CachedProjectMapper
import com.worldsnas.cache.model.CachedProject
import com.worldsnas.cache.model.CachedProject_
import com.worldsnas.cache.model.Config
import com.worldsnas.data.model.ProjectEntity
import com.worldsnas.data.repository.ProjectsCache
import io.objectbox.BoxStore
import io.objectbox.rx.RxQuery
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

class ProjectsCacheImpl @Inject constructor(
        private val store: BoxStore,
        private val mapper: CachedProjectMapper
) : ProjectsCache {
    override fun clearProjects(): Completable {
        return Completable.defer {
            val projectBox = store.boxFor(CachedProject::class.java)
            projectBox.removeAll()
            projectBox.closeThreadResources()
            Completable.complete()
        }
    }

    override fun saveProjects(projects: List<ProjectEntity>): Completable {
        return Completable.defer {
            val projectBox = store.boxFor(CachedProject::class.java)
            projectBox.put(projects.map { mapper.mapToCached(it) })
            projectBox.closeThreadResources()
            Completable.complete()
        }
    }

    override fun getProjects(): Observable<List<ProjectEntity>> {
        return Observable.defer {
            Observable.using({
                store.boxFor(CachedProject::class.java)
            }, {
                RxQuery.observable(it.query().build())
            }, {
                it.closeThreadResources()
            })
        }
                .map { cachedProjects ->
                    cachedProjects.map { cachedProject ->
                        mapper.mapFromCached(cachedProject)
                    }
                }
    }

    override fun getBookmarkProjects(): Observable<List<ProjectEntity>> {
        return Observable.defer {
            Observable.using({
                store.boxFor(CachedProject::class.java)
            }, {
                RxQuery.observable(it.query().equal(CachedProject_.isBookmarked, true).build())
            }, {
                it.closeThreadResources()
            })
        }
                .map { cachedProjects ->
                    cachedProjects.map { cachedProject ->
                        mapper.mapFromCached(cachedProject)
                    }
                }
    }

    override fun setProjectAsBookmark(projectId: String): Completable {
        return Observable.defer {
            Observable.just(projectId)
        }
                .map { it.toLong() }
                .map {
                    val projectBox = store.boxFor(CachedProject::class.java)
                    val project = projectBox.get(it)
                    if (project != null) {
                        project.copy(isBookmarked = true)
                        projectBox.put(project)
                    }
                    projectBox.closeThreadResources()
                    it
                }
                .ignoreElements()
    }

    override fun setProjectAsNotBootkmark(projectId: String): Completable {
        return Observable.just(projectId)
                .map { it.toLong() }
                .map {
                    val projectBox = store.boxFor(CachedProject::class.java)
                    val project = projectBox.get(it)
                    project.copy(isBookmarked = false)
                    projectBox.put(project)
                    projectBox.closeThreadResources()
                }
                .ignoreElements()
    }

    override fun areProjectsCached(): Single<Boolean> {
        return Observable.using({
            store.boxFor(CachedProject::class.java)
        }, {
            Observable.just(it.count() > 0)
        }, {
            it.closeThreadResources()
        }).singleOrError()
    }

    override fun setLastCacheTime(lastCache: Long): Completable {
        return Completable.defer({
            val configBox = store.boxFor(Config::class.java)
            configBox.removeAll()
            configBox.put(Config(lastCacheTime = lastCache))
            configBox.closeThreadResources()
            Completable.complete()
        })
    }

    override fun isProjectCacheExpired(): Single<Boolean> {
        return Single.defer {
            Single.create { it: SingleEmitter<Boolean> ->
                val currentTime = System.currentTimeMillis()
                val expirationTime = 60 * 10 * 1000
                val configBox = store.boxFor(Config::class.java)
                val configs = configBox.all

                val isExpired: Boolean

                isExpired = if (configs.size > 0) {
                    val config = configs[0]
                    if (config == null) {
                        true
                    } else {
                        currentTime - config.lastCacheTime > expirationTime
                    }
                } else {
                    true
                }

                it.onSuccess(isExpired)
            }
        }
    }

}