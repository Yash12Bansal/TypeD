package com.alpha.typed.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alpha.typed.Repositories.BloodGlucoseRepo
import com.alpha.typed.Repositories.ExercisesRepo
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityMainScreenBinding
import java.util.Date

class BloodGlucoseViewModel:ViewModel() {

    var repo= BloodGlucoseRepo()

    fun postBloodGlucose(loading:Loading<ActivityMainScreenBinding>, email:String,value:Double,bg:String,time:String,date:Date){
        repo.postBloodGlucose(loading,email,value,bg,time,date)
    }
}