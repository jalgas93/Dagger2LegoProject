package com.example.daggerlegoproject.presentations.FirstFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.daggerlegoproject.data.repositories.Repos
import com.example.daggerlegoproject.domain.modelRetrofit.Result
import com.example.daggerlegoproject.domain.modelRetrofit.RetrofitModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class FrontViewModel @Inject constructor(
    var repos: Repos,
    private @Named("auth_token") var token: String
) : ViewModel() {

//    private val data: MutableLiveData<RetrofitModel> = MutableLiveData()
//    val liveData: LiveData<RetrofitModel> = data

//     fun legoSet(page: Int,query:String) {
//        viewModelScope.launch {
//           data.value =  repos.repo(token, page,query)
//        }
//    }

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Result>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<Result>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null){
            return lastResult
        }
        currentQueryValue = queryString
        val newResult:Flow<PagingData<Result>> = repos.getSearchResult(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult

    }

}