package com.example.daggerlegoproject.domain.remoteKeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE LegoId = :legoId ")
    suspend fun findId(legoId: String): RemoteKeys

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}