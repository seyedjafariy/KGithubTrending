package com.worldsnas.cache.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class CachedProject (
        @Id var id: Long = 0,
        val name: String, val fullName: String,
        val starCount: String, val dateCreated: String,
        val ownerName: String, val ownerAvatar: String,
        val isBookmarked: Boolean
)

