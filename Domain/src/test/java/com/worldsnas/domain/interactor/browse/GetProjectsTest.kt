package com.worldsnas.domain.interactor.browse

import com.worldsnas.domain.executor.PostExecutionThread
import com.worldsnas.domain.repository.ProjectRepository
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GetProjectsTest {

    private lateinit var getProjects: GetProjects
    @Mock
    lateinit var projectRepository: ProjectRepository
    @Mock
    lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        getProjects = GetProjects(projectRepository, postExecutionThread)

    }

}