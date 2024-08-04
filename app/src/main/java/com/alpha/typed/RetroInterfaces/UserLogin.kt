package com.alpha.typed.RetroInterfaces
import com.alpha.typed.Models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserLogin {
    @POST("/auth")
    fun auth(@Body user: User): Call<ResponseBody>?
}
