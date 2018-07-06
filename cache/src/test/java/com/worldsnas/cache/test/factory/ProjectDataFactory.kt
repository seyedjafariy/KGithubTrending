package com.worldsnas.cache.test.factory

import com.worldsnas.cache.model.CachedProject
import com.worldsnas.data.model.ProjectEntity

object ProjectDataFactory {

    fun makeCacheProject(): CachedProject {
        return CachedProject(DataFactory.randomLong(), DataFactory.randomUuid(),
                DataFactory.randomUuid(), DataFactory.randomUuid(),
                DataFactory.randomUuid(), DataFactory.randomUuid(),
                DataFactory.randomUuid(), DataFactory.randomBoolean())
    }

    fun makeProjectEntity(): ProjectEntity {
        return ProjectEntity(DataFactory.randomUuid(), DataFactory.randomUuid(),
                DataFactory.randomUuid(), DataFactory.randomUuid(),
                DataFactory.randomUuid(), DataFactory.randomUuid(),
                DataFactory.randomUuid(), DataFactory.randomBoolean())
    }
}