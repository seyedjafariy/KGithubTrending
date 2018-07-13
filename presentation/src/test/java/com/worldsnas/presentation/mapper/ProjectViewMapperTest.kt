package com.worldsnas.presentation.mapper

import com.worldsnas.domain.model.Project
import com.worldsnas.presentation.factory.ProjectFactory
import com.worldsnas.presentation.model.ProjectView
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProjectViewMapperTest {

    private val mapper = ProjectViewMapper()

    @Test
    fun mapToViewTest(){
        val project = ProjectFactory.makeProject()

        val view = mapper.mapToView(project)

        assertEqualProjectView(view, project)
    }

    private fun assertEqualProjectView(view : ProjectView, data : Project){
        assertEquals(data.id, view.id)
        assertEquals(data.name, view.name)
        assertEquals(data.starCount, view.starCount)
        assertEquals(data.fullName, view.fullName)
        assertEquals(data.dateCreated, view.dateCreated)
        assertEquals(data.ownerAvatar, view.ownerAvatar)
        assertEquals(data.ownerName, view.ownerName)
        assertEquals(data.isBookmarked, view.isBookmarked)

    }

}