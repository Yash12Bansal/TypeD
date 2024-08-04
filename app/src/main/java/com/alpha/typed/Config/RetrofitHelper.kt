package com.alpha.typed.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
//    ""http://localhost:8080"
//    "https://zealous-gear-ant.cyclic.app"
    val baseUrl = "https://t1-expert-be.onrender.com"
    
    fun getInstance(): Retrofit {

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())

            // we need to add converter factory to
//             convert JSON object to Java object
            .build()
    }
}