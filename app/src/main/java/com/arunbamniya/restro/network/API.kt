package com.arunbamniya.restro.network

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    @GET("api/v1/category")
    fun listCategory(): Call<List<CategoryResponse>?>?

    @GET("api/v1/item")
    fun getItems(@Query("category") category: String): Call<MutableList<ItemResponse>?>?

    @GET("api/v1/order/{order_id}/payment_status")
    fun getStatus(@Path("order_id") order_id: String): Call<ItemResponse>?

    @JvmSuppressWildcards
    @POST("api/v1/order")
    fun getQRCode(@Body list: List<orders>): Call<QResponse>?

}