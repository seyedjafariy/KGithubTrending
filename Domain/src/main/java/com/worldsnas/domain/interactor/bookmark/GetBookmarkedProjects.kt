package com.worldsnas.domain.interactor.bookmark

import com.worldsnas.domain.executor.PostExecutionThread
import com.worldsnas.domain.interactor.ObservableUseCase
import com.worldsnas.domain.model.Project
import com.worldsnas.domain.repository.ProjectRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetBookmarkedProjects @Inject constructor(
        private val projectsRepository: ProjectRepository,
        postExecutionThread: PostExecutionThread):
ObservableUseCase<List<Project>, Nothing>(postExecutionThread){

    override fun buildUseCaseObservable(params: Nothing?): Observable<List<Project>> {
        return projectsRepository.getBookmarkedProjects()
    }

}