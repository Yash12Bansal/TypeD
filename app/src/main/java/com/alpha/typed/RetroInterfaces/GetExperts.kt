package com.alpha.typed.RetroInterfaces

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface GetExperts {
    @GET("/getExperts")
    fun getExperts(): Call<List<JsonObject>>?
}