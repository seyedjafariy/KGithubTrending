package com.worldsnas.cache.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Config (@Id var id: Long = 0, val lastCacheTime : Long)
