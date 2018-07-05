package com.worldsnas.domain.interactor.bookmark

import com.worldsnas.domain.executor.PostExecutionThread
import com.worldsnas.domain.interactor.CompletableUseCase
import com.worldsnas.domain.repository.ProjectRepository
import io.reactivex.Completable
import javax.inject.Inject

class UnbookmarkProject @Inject constructor(
        private val projectRepository: ProjectRepository,
        postExecutionThread: PostExecutionThread) :
        CompletableUseCase<UnbookmarkProject.Params>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Params?): Completable {
        if (params == null) {
            throw IllegalArgumentException("Params can not be null!")
        }

        return projectRepository.unbookmarkProject(params.projectId)
    }

    data class Params constructor(val projectId: String) {
        companion object {
            fun forProject(projectId: String): Params {
                return Params(projectId)
            }
        }

    }
}