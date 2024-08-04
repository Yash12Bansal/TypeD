package com.alpha.typed.RetroInterfaces

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface DownloadPDF {
    @GET("/downloadPDF/{id}")
    @Streaming
    fun downloadPdf(@Path("id") email:String): Call<ResponseBody>?
}