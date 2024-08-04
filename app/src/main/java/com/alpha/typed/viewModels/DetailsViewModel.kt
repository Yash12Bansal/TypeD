package com.alpha.typed.viewModels

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.Repositories.DetailsRepo
import com.alpha.typed.Repositories.InitialParamsRepo
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityEnterDetailsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class DetailsViewModel(context:Application):AndroidViewModel(context){

    var repo=DetailsRepo(context)
    var repo2=InitialParamsRepo(context)
    private val _name= MutableLiveData<String>("")
    val name:LiveData<String> =_name

    private val _breakfast_icr= MutableLiveData<String>("NULL")
    val breakfast_icr:LiveData<String> =_breakfast_icr

    private val _lunch_icr= MutableLiveData<String>("")
    val lunch_icr:LiveData<String> =_lunch_icr

    private val _snack_icr= MutableLiveData<String>("")
    val snack_icr:LiveData<String> =_snack_icr

    private val _dinner_icr= MutableLiveData<String>("")
    val dinner_icr:LiveData<String> =_dinner_icr


    private val _breakfast_isf= MutableLiveData<String>("")
    val breakfast_isf:LiveData<String> =_breakfast_isf

    private val _lunch_isf= MutableLiveData<String>("")
    val lunch_isf:LiveData<String> =_lunch_isf

    private val _snack_isf= MutableLiveData<String>("")
    val snack_isf:LiveData<String> =_snack_isf

    private val _dinner_isf= MutableLiveData<String>("")
    val dinner_isf:LiveData<String> =_dinner_isf

    private val _contact= MutableLiveData<String>("")
    val contact:LiveData<String> =_contact

    private val _dob= MutableLiveData<String>("")
    val dob:LiveData<String> =_dob

    private val _sex= MutableLiveData<String>("")
    val sex:LiveData<String> =_sex

    private val _weight= MutableLiveData<String>("")
    val weight:LiveData<String> =_weight

    private val _height= MutableLiveData<String>("")
    val height:LiveData<String> =_height

    private val _yod= MutableLiveData<String>("")
    val yod:LiveData<String> =_yod

    private val _doctorEmail= MutableLiveData<String>("")
    val doctorEmail:LiveData<String> =_doctorEmail

    private val _doctorPhone= MutableLiveData<String>("")
    val doctorPhone:LiveData<String> =_doctorPhone

    private val _insulinDoses= MutableLiveData<String>("5.0 units")
    val insulinDoses:LiveData<String> =_insulinDoses

    private val _hs= MutableLiveData<String>("55.0 cm")
    val hs:LiveData<String> =_hs

    private val _ws= MutableLiveData<String>("5.0 kg")
    val ws:LiveData<String> =_ws

    private val _cm= MutableLiveData<String>("55")
    val cm:LiveData<String> =_cm

    private val _mm= MutableLiveData<String>("0")
    val mm:LiveData<String> =_mm

    private val _kg= MutableLiveData<String>("5")
    val kg:LiveData<String> =_kg

    private val _gm= MutableLiveData<String>("0")
    val gm:LiveData<String> =_gm

    private val _doseIntegral= MutableLiveData<String>("5")
    val doseIntegral:LiveData<String> =_doseIntegral

    private val _doseDecimal= MutableLiveData<String>("0")
    val doseDecimal:LiveData<String> =_doseDecimal


    fun setCm(newVal:String){
        _cm.value=newVal
        _hs.value=_cm.value+"."+_mm.value+" cm"
    }

    fun setMm(newVal:String){
        _mm.value=newVal
        _hs.value=_cm.value+"."+_mm.value+" cm"
    }

    fun setDI(newVal:String){
        _doseIntegral.value=newVal
        _insulinDoses.value=_doseIntegral.value+"."+_doseDecimal.value+" units"
    }
    fun setDD(newVal:String){
        _doseDecimal.value=newVal
        _insulinDoses.value=_doseIntegral.value+"."+_doseDecimal.value+" units"
    }

    fun setKg(newVal:String){
        _kg.value=newVal
        _ws.value=_kg.value+"."+_gm.value+" kg"
    }

    fun setGm(newVal:String){
        _gm.value=newVal
        _ws.value=_kg.value+"."+_gm.value+" kg"
    }
    fun setBreakfastIcr(breakfast_icr:String){
        _breakfast_icr.value=breakfast_icr
    }
    fun setLunchIcr(lunch_icr:String){
        _lunch_icr.value=lunch_icr
    }
    fun setSnackIcr(snack_icr:String){
        _snack_icr.value=snack_icr
    }
    fun setDinnerIcr(dinner_icr:String){
        _dinner_icr.value=dinner_icr
    }

    fun setName(name:String){
        _name.value=name
    }

    fun setContact(contact:String){
        _contact.value=contact
    }

    fun setSex(sex:String){
        _sex.value=sex
    }

    fun setDob(dob:String){
        _dob.value=dob
    }

    fun setHeight(height:String){
        _height.value=height
    }

    fun setWeight(weight:String){
        _weight.value=weight
    }

    fun setDoctorEmail(doctorEmail:String){
        _doctorEmail.value=doctorEmail
    }

    fun setDoctorPhone(doctorPhone:String){
        _doctorPhone.value=doctorPhone
    }

    fun setYod(yod:String){
        _yod.value=yod
    }

    fun setInsulinDoses(insulinDoses:String){
        _insulinDoses.value=insulinDoses
    }

    fun postDetails(
        loading:Loading<ActivityEnterDetailsBinding>,
         activity:FragmentActivity,
         email:String,
         name:String,
         phone:String,
         gender:String,
         dob: Date,
         weight:Double,
         height:Double,
         doctorName:String,
         doses:Double,
         yearOfDiagnosis: Date,
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
        CoroutineScope(Dispatchers.IO).launch {
            repo.postDetails(
                loading,
                activity,
                email,
                name,
                phone,
                gender,
                dob,
                weight,
                height,
                doctorName,
                doses,
                yearOfDiagnosis,
                breakfastICR,
                lunchICR,
                snackICR,
                dinnerICR,
                breakfastISF,
                lunchISF,
                snackISF,
                dinnerISF,
                ICR,
                ISF,

                )

        }
    }
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

        Log.e("SSS","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDOIFDFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ1909090909909009A")
        CoroutineScope(Dispatchers.IO).launch {
            repo2.postInitialParams(
                activity,
                email,
                breakfastICR,
                lunchICR,
                snackICR,
                dinnerICR,
                breakfastISF,
                lunchISF,
                snackISF,
                dinnerISF,
                ICR,
                ISF,
                )

        }
    }

}










