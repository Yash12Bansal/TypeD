package com.alpha.typed.Repositories

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.alpha.typed.Activities.RequestSentActivity
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Fragments.Details10
import com.alpha.typed.Models.Details
import com.alpha.typed.Models.PredictionTrainedParams
import com.alpha.typed.Navigation_drawer.MainScreen
import com.alpha.typed.RetroInterfaces.AddDetails
import com.alpha.typed.RetroInterfaces.Prediction
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityEnterDetailsBinding
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class DetailsRepo(var context:Application){
    lateinit var details:Details
    lateinit var api : AddDetails
    var res=false
    lateinit var initialParams: PredictionTrainedParams
    lateinit var api2 : Prediction

    fun postDetails(
        loading: Loading<ActivityEnterDetailsBinding>,
        activity:FragmentActivity,
        email:String,
        name:String,
        phone:String,
        gender:String,
        dobN:Date,
        weightN:Double,
        heightN:Double,
        doctorName:String,
        dosesN:Double,
        yodN:Date,
        breakfastICR:Double,
        lunchICR:Double,
        snackICR:Double,
        dinnerICR:Double,
        breakfastISF:Double,
        lunchISF:Double,
        snackISF:Double,
        dinnerISF:Double,
        ICR:Double,
        ISF:Double,

        ){
        api = RetrofitHelper.getInstance().create(AddDetails::class.java)
        api2 = RetrofitHelper.getInstance().create(Prediction::class.java)

        details = Details(email?:"NULL",name?:"NULL",phone?:"NULL",gender?:"NULL",dobN,weightN?:0.0,heightN?:0.0,doctorName?:"NULL",dosesN?:0.0,yodN)
        api.addDetails(details)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                initialParams = PredictionTrainedParams(email?:"NULL",breakfastICR, breakfastISF,lunchICR, lunchISF,snackICR,snackISF,dinnerICR,dinnerISF,ICR,ISF)
                api2.updateParamsAndroid(initialParams)?.enqueue(object : Callback<ResponseBody> {

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.e("hts","response called")
                        res=true
                        var str=response.body()?.string()
                        val jsonObject: JsonObject =
                            JsonParser().parse(str).getAsJsonObject()
                        var xx=jsonObject.get("msg")
                        Log.e("this",jsonObject.toString())
                        if (str != null) {
                            Log.e("tjs",str)
                        }
                        loading.stopLoading()
                        Log.e("DF","fkdfkjfkdfjkfkjdfjkdfkjdfjkdjfkdkjfdjkf"+jsonObject.get("code").toString())
                        if((jsonObject.get("code").toString()).equals("0")){

//                if((jsonObject.get("code").toString()).equals("\"Nahi tha re User\"")){

                            context.startActivity(
                                Intent(context, RequestSentActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                            activity.finish()
                        }
                        else{
                            context.startActivity(
                                Intent(context, MainScreen::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                            activity?.finish()

                        }
                    }
                    
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        res=false
                        Log.e("SSS","FAILDFUUxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDOIFDFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ1909090909909009A"+call)
                        Log.e("SSS","FAILDFUUxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDOIFDFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ1909090909909009A"+t)

                    }
                })
//////
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                res=false
            }
        })
        Log.e("hts","AsYnC called")

    }
}