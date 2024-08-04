package com.alpha.typed.RetroInterfaces

import com.alpha.typed.Models.AcceptedAPI
import com.alpha.typed.Models.PredictionExtraDetails
import com.alpha.typed.Models.PredictionTrainedParams
import com.alpha.typed.Models.PredictionValuesStatus
import com.alpha.typed.Models.RejectedAPI
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Prediction {
    @POST("/prediction/entry")
    fun addPrediction(@Body data:PredictionValuesStatus):Call<ResponseBody>?

    @POST("/prediction/updateParamsAndroid")
    fun updateParamsAndroid(@Body data:PredictionTrainedParams):Call<ResponseBody>?

    @POST("/prediction/extraDetails")
    fun addExtraDetails(@Body data:PredictionExtraDetails):Call<ResponseBody>?

    @GET("/prediction/predictionStatus/{email}")
    fun getPredictionStatus(@Path("email") email:String):Call<ResponseBody>?

    @GET("/prediction/getPredictionParams/{email}")
    fun getPredictionParams(@Path("email") email:String):Call<JsonObject>?

    @POST("/prediction/accepted")
    fun acceptPrediction(@Body data:AcceptedAPI):Call<ResponseBody>?

    @POST("/prediction/rejected")
    fun rejectPrediction(@Body data:RejectedAPI):Call<ResponseBody>?

}