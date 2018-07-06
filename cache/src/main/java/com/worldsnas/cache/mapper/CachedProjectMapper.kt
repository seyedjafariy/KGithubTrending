package com.worldsnas.cache.mapper

import com.worldsnas.cache.model.CachedProject
import com.worldsnas.data.model.ProjectEntity
import javax.inject.Inject

class CachedProjectMapper @Inject constructor(): CacheMapper<CachedProject, ProjectEntity>{

    override fun mapFromCached(type: CachedProject): ProjectEntity {
        return ProjectEntity(type.id.toString(), type.name, type.fullName, type.starCount, type.dateCreated, type.ownerName, type.ownerAvatar, type.isBookmarked)
    }

    override fun mapToCached(type: ProjectEntity): CachedProject {
        return CachedProject(type.id.toLong(), type.name, type.fullName, type.starCount, type.dateCreated, type.ownerName, type.ownerAvatar, type.isBookmarked)
    }
}