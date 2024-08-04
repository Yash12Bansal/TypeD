package com.alpha.typed.Repositories

import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alpha.typed.Classes.FirebaseUserInfo
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Models.ExerciseEntry
import com.alpha.typed.RetroInterfaces.Exercises
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityMainScreenBinding
import com.alpha.typed.viewModels.ExerciseViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class ExercisesRepo {
    lateinit var api : Exercises
    var exercise_name = MutableLiveData<MutableList<String>>()
    var x= mutableListOf<String>()
    fun postExercise(loading: Loading<ActivityMainScreenBinding>,key:String,current__time:String,final_time_end:String,final_time_start:String,finalDateN:Date,exercise:String){
        var exerciseEntry= ExerciseEntry(key?:"NULL",current__time,final_time_end,final_time_start,finalDateN,exercise)
        api = RetrofitHelper.getInstance().create(Exercises::class.java)
        api.postExercise(exerciseEntry)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                loading.stopLoading()
                var str=response.body()?.string()
                val jsonObject: JsonObject =
                    JsonParser().parse(str).getAsJsonObject()
                var xx=jsonObject.get("msg")
//                Toast.makeText(activity!!,""+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
                if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){
//                                        loading.stopLoading()
                }
                else{
                    //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        requireContext().finish()

                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("gee",""+t.stackTrace)
//                    Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()

                Log.e("gee","onFailureeeee"+t.message)

                // handle the failure
            }
        })

    }
//    :MutableList<String>
    fun getExercises(_exercise_name:MutableLiveData<MutableList<String>>,alertDialog: AlertDialog, loading: Loading<ActivityMainScreenBinding>,convertView:View):MutableLiveData<MutableList<String>>{
        api = RetrofitHelper.getInstance().create(Exercises::class.java)
        api.getExercises()?.enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                var list=response.body() as List<JsonObject>

                val gson = Gson()

                for(i in list){
                    var eachExercise = gson.fromJson(i, FirebaseUserInfo::class.java)

                    x.add(eachExercise.A)
                    Log.e("lUIRIEURIEIRUE","RJKLJKJKJKLJKLJKLKllllllllllll"+"fdlfdjklfdjkfkjl"+i)
                    Log.e("lUIRIEURIEIRUE","RJKLJKJKJKLJKLJKLKllllllllllll"+"fdlfdjklfdjkfkjl"+eachExercise.A)

                }
                if(_exercise_name.value==null){
                    _exercise_name.value=x
                }
                alertDialog.setView(convertView)
                loading.stopLoading()
                alertDialog.show()

            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Log.e("gee",""+t.stackTrace)
                Toast.makeText(alertDialog.context,"Could not load the list.\nTry again.",Toast.LENGTH_LONG).show()

                loading.stopLoading()
                alertDialog.show()

                Log.e("gee","onFailureeeee"+t.message)
            }
        })
        return exercise_name
    }

}