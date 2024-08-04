package com.alpha.typed.RetroInterfaces

import com.alpha.typed.Models.BloodGlucoseEntry
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BloodGlucose {
    @POST("/bgEntry")
    fun postBgEntry(@Body bgEntry:BloodGlucoseEntry): Call<ResponseBody>?
}