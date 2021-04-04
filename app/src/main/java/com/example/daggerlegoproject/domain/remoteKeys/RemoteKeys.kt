package com.example.daggerlegoproject.domain.remoteKeys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val legoId: String,
    val prevKey: Int?,
    val nextKey: Int?
)