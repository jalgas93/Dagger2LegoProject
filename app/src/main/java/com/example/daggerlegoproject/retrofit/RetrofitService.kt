package com.example.daggerlegoproject.retrofit

import com.example.daggerlegoproject.domain.modelRetrofit.RetrofitModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
const val IN_QUALIFIER = "in:name"
interface RetrofitService {
    companion object {
        const val ENDPOINT = "https://rebrickable.com/api/v3/lego/"
    }

    @GET("sets")
    suspend fun getSets(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String

    ): RetrofitModel
}