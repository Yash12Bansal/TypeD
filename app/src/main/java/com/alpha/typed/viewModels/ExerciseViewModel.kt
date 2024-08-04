package com.alpha.typed.viewModels

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alpha.typed.Classes.FirebaseUserInfo
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Repositories.ExercisesRepo
import com.alpha.typed.RetroInterfaces.Exercises
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityMainScreenBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class ExerciseViewModel:ViewModel() {

    var repo=ExercisesRepo()
    lateinit var api : Exercises

    var _exercise_name = MutableLiveData<MutableList<String>>()
    var exercise_name:LiveData<MutableList<String>> =_exercise_name

    fun getExercises(alertDialog: android.app.AlertDialog, loading: Loading<ActivityMainScreenBinding>,convertView: View){
         repo.getExercises(_exercise_name,alertDialog,loading,convertView)
    }


    fun postExercise(loading:Loading<ActivityMainScreenBinding>,key:String, current__time:String, final_time_end:String, final_time_start:String, finalDateN: Date, exercise:String){
        repo.postExercise(loading,key?:"NULL",current__time,final_time_end,final_time_start,finalDateN,exercise)
    }
}