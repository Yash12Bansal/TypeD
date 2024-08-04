package com.alpha.typed.RetroInterfaces

import com.alpha.typed.Models.InsulinEntry
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Insulin {
    @POST("/insulinEntry")
    fun postInsulinEntry(@Body insulin: InsulinEntry): Call<ResponseBody>?
}