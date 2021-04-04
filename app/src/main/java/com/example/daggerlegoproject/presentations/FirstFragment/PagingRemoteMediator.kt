package com.example.daggerlegoproject.presentations.FirstFragment

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.daggerlegoproject.data.room.AppDatabase
import com.example.daggerlegoproject.domain.modelRetrofit.Result
import com.example.daggerlegoproject.domain.remoteKeys.RemoteKeys
import com.example.daggerlegoproject.retrofit.IN_QUALIFIER
import com.example.daggerlegoproject.retrofit.RetrofitService
import java.io.InvalidObjectException
import javax.inject.Inject

const val STARTING_PAGE_INDEX = 1
@OptIn(ExperimentalPagingApi::class)
class PagingRemoteMediator @Inject constructor(
    private val query: String,
    private val service: RetrofitService,
    private val appDatabase: AppDatabase,
    private val token:String
) : RemoteMediator<Int, Result>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    // LoadType - PREPEND, поэтому некоторые данные были загружены раньше,
                    // чтобы мы могли получить удаленные ключи
                    // Если удаленные ключи равны нулю, значит, мы недопустимое состояние и у нас есть ошибка
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException(" Remote key will not be null")
                }
                remoteKeys.nextKey
            }
        }
        val apiQuery = query+IN_QUALIFIER

        try {
            val responce = service.getSets(token, page, apiQuery)
            val repos = responce.results
            val endOfPaginationReached = repos!!.isEmpty()
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteDao().clearRemoteKeys()
                    appDatabase.roomDao().clearLegoDatabase()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeys(
                        legoId = it.id, prevKey = prevKey, nextKey = nextKey
                    )
                }
                appDatabase.remoteDao().insertKeys(keys)
                appDatabase.roomDao().insertAll(repos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }



    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Result>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                appDatabase.remoteDao().findId(repo.id)
            }
    }





    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Result>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                appDatabase.remoteDao().findId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Result>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.remoteDao().findId(repoId)
            }
        }
    }
}