package com.arunbamniya.restro.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("api/v1/category")
    fun listCategory(): Call<List<CategoryResponse>?>?

    @GET("api/v1/item")
    fun getItems(@Query("category") category : String): Call<MutableList<ItemResponse>?>?

}