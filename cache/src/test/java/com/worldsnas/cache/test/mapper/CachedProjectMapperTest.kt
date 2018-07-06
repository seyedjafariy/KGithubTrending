package com.worldsnas.cache.test.mapper

import com.worldsnas.cache.mapper.CachedProjectMapper
import com.worldsnas.cache.model.CachedProject
import com.worldsnas.cache.test.factory.ProjectDataFactory
import com.worldsnas.data.model.ProjectEntity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class CachedProjectMapperTest {

    private val mapper = CachedProjectMapper()

    @Test
    fun mapFromCachedMapsData() {
        val cached = ProjectDataFactory.makeCacheProject()

        val mapped = mapper.mapFromCached(cached)

        assertEqualData(mapped, cached)
    }

    @Test
    fun mapToCachedMapsData() {
        val entity = ProjectDataFactory.makeProjectEntity()

        val mapped = mapper.mapToCached(entity)

        assertEqualData(entity, mapped)
    }

    private fun assertEqualData(entity: ProjectEntity, cached: CachedProject) {
        assertEquals(cached.id, entity.id.toLong())
        assertEquals(cached.name, entity.name)
        assertEquals(cached.starCount, entity.starCount)
        assertEquals(cached.dateCreated, entity.dateCreated)
        assertEquals(cached.fullName, entity.fullName)
        assertEquals(cached.isBookmarked, entity.isBookmarked)
        assertEquals(cached.ownerAvatar, entity.ownerAvatar)
        assertEquals(cached.ownerName, entity.ownerName)

    }
}