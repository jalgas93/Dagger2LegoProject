package com.example.daggerlegoproject.data.room

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.daggerlegoproject.domain.modelRetrofit.Result
import com.example.daggerlegoproject.domain.modelRetrofit.RetrofitModel

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(retrofitModel: List<Result>)
    @Query(
        "SELECT * FROM LegoDatabase WHERE " +
                "name LIKE:queryString")
    fun findByName(queryString: String): PagingSource<Int, Result>

    @Query("DELETE FROM LegoDatabase")
    suspend fun clearLegoDatabase()

}