package com.alpha.typed.Repositories

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Fragments.Details10
import com.alpha.typed.Models.Details
import com.alpha.typed.Models.PredictionTrainedParams
import com.alpha.typed.Navigation_drawer.MainScreen
import com.alpha.typed.RetroInterfaces.AddDetails
import com.alpha.typed.RetroInterfaces.Prediction
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class InitialParamsRepo(var context:Application){
    lateinit var initialParams:PredictionTrainedParams
    lateinit var api : Prediction
    var res=false

    fun postInitialParams(
        activity:FragmentActivity,
        email:String,
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
        Log.e("SSS","asaaaxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDOIFDFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ1909090909909009A")

        api = RetrofitHelper.getInstance().create(Prediction::class.java)
        initialParams = PredictionTrainedParams(email?:"NULL",breakfastICR, breakfastISF,lunchICR, lunchISF,snackICR,snackISF,dinnerICR,dinnerISF,ICR,ISF)
        api.updateParamsAndroid(initialParams)?.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e("d","response called09438948390489309804904904389803484390")
                res=true
                var str=response.body()?.string()
                Log.e("sss",str+"TJKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKIERIEWOREWJIREJIROIJ9999")
                context.startActivity(
                    Intent(context, MainScreen::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                activity.finish()

            //                val jsonObject: JsonObject =
//                    JsonParser().parse(str).getAsJsonObject()
//                var xx=jsonObject.get("msg")
//                if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){

//                    context.startActivity(
//                        Intent(context, MainScreen::class.java).addFlags(
//                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                    activity.finish()
//                }
//                else{

//                    context.startActivity(
//                        Intent(context, MainScreen::class.java).addFlags(
//                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                    activity?.finish()

//                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                res=false
                Log.e("SSS","FAILDFUUxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDOIFDFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ1909090909909009A"+call)
                Log.e("SSS","FAILDFUUxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDOIFDFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ1909090909909009A"+t)

            }
        })
        Log.e("hts","AsYnC called")
    }
}