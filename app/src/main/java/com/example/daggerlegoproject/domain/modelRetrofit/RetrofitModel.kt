package com.example.daggerlegoproject.domain.modelRetrofit


import com.google.gson.annotations.SerializedName

data class RetrofitModel(
    @SerializedName("count")
    var count: Int, // 17457
    @SerializedName("next")
    var next: String?, // https://rebrickable.com/api/v3/lego/sets/?page=2
    @SerializedName("previous")
    var previous: Any?, // null
    @SerializedName("results")
    var results: List<Result>?
)