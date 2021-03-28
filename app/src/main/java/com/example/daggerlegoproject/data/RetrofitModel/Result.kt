package com.example.daggerlegoproject.data.RetrofitModel


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("set_num")
    var setNum: String?, // 001-1
    @SerializedName("name")
    var name: String?, // Gears
    @SerializedName("year")
    var year: Int, // 1965
    @SerializedName("theme_id")
    var themeId: Int, // 1
    @SerializedName("num_parts")
    var numParts: Int, // 43
    @SerializedName("set_img_url")
    var setImgUrl: String?, // https://cdn.rebrickable.com/media/sets/001-1/11530.jpg
    @SerializedName("set_url")
    var setUrl: String?, // https://rebrickable.com/sets/001-1/gears/
    @SerializedName("last_modified_dt")
    var lastModifiedDt: String? // 2018-05-05T20:39:47.277922Z
)