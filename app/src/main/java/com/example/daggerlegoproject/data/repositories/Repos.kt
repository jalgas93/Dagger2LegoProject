package com.example.daggerlegoproject.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.daggerlegoproject.data.room.AppDatabase
import com.example.daggerlegoproject.domain.modelRetrofit.Result
import com.example.daggerlegoproject.presentations.FirstFragment.PagingRemoteMediator
import com.example.daggerlegoproject.retrofit.RetrofitService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repos @Inject constructor(
    var retrofitService: RetrofitService,
    var appDatabase: AppDatabase

) {

    // suspend fun repo(token:String,page:Int,query:String) = retrofitService.getSets(token, page,query)
    fun getSearchResult(query: String): Flow<PagingData<Result>> {


        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = {
            TODO()
            // appDatabase.roomDao().findByName(dbQuery)
            }

            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
                remoteMediator = PagingRemoteMediator(query, retrofitService, appDatabase),
                pagingSourceFactory = pagingSourceFactory
            ).flow
        }



    companion object {
        private const val NETWORK_PAGING_SIZE = 50
    }
}