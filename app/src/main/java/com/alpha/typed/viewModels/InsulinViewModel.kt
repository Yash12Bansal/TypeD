package com.alpha.typed.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alpha.typed.Repositories.ExercisesRepo
import com.alpha.typed.Repositories.InsulinRepo
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityMainScreenBinding
import java.util.Date

class InsulinViewModel:ViewModel() {

    var repo= InsulinRepo()

    fun postInsulin(loading: Loading<ActivityMainScreenBinding>, email:String, insulinAmount:Double, category: String, correctionDose:Double, date:Date, time:String, type:String){
        repo.postInsulin(loading,email,insulinAmount,category,correctionDose,date,time,type)
    }
}