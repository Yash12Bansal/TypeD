package com.alpha.typed.RetroInterfaces

import com.alpha.typed.Models.Food
import com.alpha.typed.Models.FoodEntry
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodInterface {

    @POST("/food/entry")
    fun addFood(@Body food: FoodEntry): Call<ResponseBody>?

    @GET("/food")
    fun getFood(): Call<List<JsonObject>>?
}