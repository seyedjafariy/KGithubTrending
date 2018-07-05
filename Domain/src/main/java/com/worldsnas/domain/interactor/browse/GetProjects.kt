package com.worldsnas.domain.interactor.browse

import com.worldsnas.domain.executor.PostExecutionThread
import com.worldsnas.domain.interactor.ObservableUseCase
import com.worldsnas.domain.model.Project
import com.worldsnas.domain.repository.ProjectRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetProjects @Inject constructor(
        private val projectsRepository: ProjectRepository,
        postExecutionThread: PostExecutionThread) :
        ObservableUseCase<List<Project>, Nothing>(postExecutionThread) {

    public override fun buildUseCaseObservable(params: Nothing?): Observable<List<Project>> {
        return projectsRepository.getProjects()
    }
}