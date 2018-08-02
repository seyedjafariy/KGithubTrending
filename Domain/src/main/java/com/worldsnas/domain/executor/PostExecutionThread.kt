package com.worldsnas.domain.executor

import io.reactivex.Scheduler

interface PostExecutionThread {
    open val scheduler : Scheduler
}