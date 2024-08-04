package com.alpha.typed.RetroInterfaces

import com.alpha.typed.Models.Details
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AddDetails {
    @POST("/details")
    fun addDetails(@Body details: Details): Call<ResponseBody>?
}