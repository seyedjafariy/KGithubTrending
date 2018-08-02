package com.worldsnas.remote.mapper

import com.worldsnas.remote.model.ProjectModel
import com.worldsnas.data.model.ProjectEntity

open class ProjectsResponseModelMapper :
        ModelMapper<ProjectModel, ProjectEntity> {

    override fun mapFromModel(model: ProjectModel): ProjectEntity {
        return ProjectEntity(model.id, model.name, model.fullName,
                model.starCount.toString(), model.dateCreated, model.owner.ownerName,
                model.owner.ownerAvatar, isBookmarked = false)
    }

}