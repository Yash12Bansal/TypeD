package com.alpha.typed.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.Classes.Constants
import com.alpha.typed.Classes.FirebaseUserInfo
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Models.Details
import com.alpha.typed.Models.Experts
import com.alpha.typed.Navigation_drawer.MainScreen
import com.alpha.typed.R
import com.alpha.typed.RetroInterfaces.AddDetails
import com.alpha.typed.RetroInterfaces.Exercises
import com.alpha.typed.RetroInterfaces.GetExperts
import com.alpha.typed.RetroInterfaces.UserLogin
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityEnterDetailsBinding
import com.alpha.typed.databinding.ActivityLoginBinding
import com.alpha.typed.databinding.FragmentDetails10Binding
import com.alpha.typed.databinding.FragmentDetails1Binding
import com.alpha.typed.databinding.FragmentDetails9Binding
import com.alpha.typed.viewModels.DetailsViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Details10 : Fragment(),EnterDetails.nextButtonListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDetails10Binding
    val sharedDetailsViewModel : DetailsViewModel by activityViewModels()
    lateinit var key:String
    lateinit var name:String
    lateinit var dob:String
    lateinit var gender:String
    lateinit var phone:String
    lateinit var yod:String
    lateinit var weight:String
    lateinit var height:String
    lateinit var doses:String
    lateinit var doctorEmail:String
    lateinit var doctorPhone:String
    lateinit var dobN:Date
    lateinit var yodN:Date
    var weightN:Double=0.0
    var heightN:Double=0.0
    var dosesN:Double=0.0
    lateinit var loading:Loading<ActivityEnterDetailsBinding>
    lateinit var breakfastIcr:String
    lateinit var lunchIcr:String
    lateinit var snackIcr:String
    lateinit var dinnerIcr:String
    var expertsList= mutableListOf<String>()

    var breakfastICR=0.0
    var lunchICR=0.0
    var snackICR=0.0
    var dinnerICR=0.0

    var breakfastISF=0.0
    var lunchISF=0.0
    var snackISF=0.0
    var dinnerISF=0.0
    var ICR=0.0
    var ISF=0.0

    lateinit var api:GetExperts


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetails10Binding.inflate(inflater,container,false)
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val spinnerAdapter = ArrayAdapter<String>(requireContext() ,android. R.layout.simple_spinner_item, android.R.id.text1)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.doctorNameSpinner?.setAdapter(spinnerAdapter)
        binding.rotateloading?.visibility=View.INVISIBLE

        loading= Loading<ActivityEnterDetailsBinding>(requireActivity(),binding.rotateloading,binding.screen)

        loading.startLoading()
        api = RetrofitHelper.getInstance().create(GetExperts::class.java)
        api.getExperts()?.enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                var list=response.body() as List<JsonObject>
                Log.e("this","PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP")
                Log.e("this",list.toString())

                val gson = Gson()

                for(i in list){
                    var eachExpert = gson.fromJson(i, Experts::class.java)

                    expertsList.add(eachExpert.email)

                }
                for(i in expertsList){
                    spinnerAdapter.add(i)
                }

                spinnerAdapter.notifyDataSetChanged()

//                if(_exercise_name.value==null){
//                    _exercise_name.value=x
//                }
//                alertDialog.setView(convertView)
                loading.stopLoading()
//                alertDialog.show()

            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Log.e("gee",""+t.stackTrace)
                Toast.makeText(requireActivity(),"Could not load the Doctor's list.\nTry again after reopening.",Toast.LENGTH_LONG).show()
                loading.stopLoading()
                Log.e("gee","onFailureeeee"+t.message)
            }
        })


        key=preferences.getString(Constants.KEY,null)?:"NULL"
        name=sharedDetailsViewModel.name.value?:"NULL"
        dob=sharedDetailsViewModel.dob.value?:"NULL"
        gender=sharedDetailsViewModel.sex.value?:"NULL"
        phone=sharedDetailsViewModel.contact.value?:"NULL"
        yod=sharedDetailsViewModel.yod.value?:"NULL"
        weight=sharedDetailsViewModel.weight.value?:"NULL"
        height=sharedDetailsViewModel.height.value?:"NULL"
        doses=sharedDetailsViewModel.insulinDoses.value?:"NULL"


//        doctorEmail=sharedDetailsViewModel.doctorEmail.value?:"NULL"
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        dobN = formatter.parse(dob)
        yodN=formatter.parse(yod)
        weightN=weight?.toDouble()?:0.0
        heightN=height?.toDouble()?:0.0
        dosesN=doses?.toDouble()?:0.0

        if(sharedDetailsViewModel.breakfast_icr.value!="NULL"){
            breakfastIcr=sharedDetailsViewModel.breakfast_icr.value?:"NULL"
            lunchIcr=sharedDetailsViewModel.lunch_icr.value?:"NULL"
            snackIcr=sharedDetailsViewModel.snack_icr.value?:"NULL"
            dinnerIcr=sharedDetailsViewModel.dinner_icr.value?:"NULL"
            ICR=breakfastIcr.toDouble()
            breakfastICR= breakfastIcr.toDouble()
            lunchICR=lunchIcr.toDouble()
            snackICR=snackIcr.toDouble()
            dinnerICR=dinnerIcr.toDouble()
        }
        else{
            var initial_ICR_500_TDD=500.0/dosesN
            var initial_ICR_weight=500.0/(0.8*weightN)
            var initial_ICR=Math.max(initial_ICR_500_TDD,initial_ICR_weight)
            breakfastICR= initial_ICR
            lunchICR=initial_ICR
            snackICR=initial_ICR
            dinnerICR=initial_ICR
            ICR=initial_ICR
        }

        breakfastISF=1800.0/dosesN
        lunchISF=1800.0/dosesN
        snackISF=1800.0/dosesN
        dinnerISF=1800.0/dosesN
        ISF=1800.0/dosesN

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity.let {
            (it as EnterDetails).obj=this
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Details10().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        activity.let {
            (it as EnterDetails).obj=this
        }
    }

    override fun onStart() {
        super.onStart()
        activity.let {
            (it as EnterDetails).obj=this
        }
    }
    override fun onNextClick() {
//        if (binding.mobileNo?.getText().toString().isEmpty()) {
//            Toast.makeText(requireContext(), "Doctor's contact number required!!", Toast.LENGTH_SHORT).show()
//            binding.doctorPhoneCard .startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.shake))
//        }
//        else {
        loading.startLoading()
//        sharedDetailsViewModel.postInitialParams(this@Details10.requireActivity(),key,breakfastICR,lunchICR,snackICR,dinnerICR,breakfastISF,lunchISF,snackISF,dinnerISF,ICR,ISF)
        sharedDetailsViewModel.postDetails(loading,this@Details10.requireActivity(),key?:"NULL",name?:"NULL",phone?:"NULL",gender?:"NULL",dobN,weightN?:0.0,heightN?:0.0,binding.doctorNameSpinner?.getSelectedItem().toString(),dosesN?:0.0,yodN,breakfastICR,lunchICR,snackICR,dinnerICR,breakfastISF,lunchISF,snackISF,dinnerISF,ICR,ISF)

//        }
    }
}