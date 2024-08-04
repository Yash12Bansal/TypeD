package com.alpha.typed.RetroInterfaces

import com.alpha.typed.Models.ExerciseEntry
import com.alpha.typed.Models.NewFoodRequest
import com.alpha.typed.Models.User
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NewFoodRequest {

    @POST("/newFoodRequest")
    fun postFoodRequest(@Body newFood:NewFoodRequest):Call<ResponseBody>?

}