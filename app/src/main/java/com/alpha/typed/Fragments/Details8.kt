package com.alpha.typed.Fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.Classes.Constants
import com.alpha.typed.R
import com.alpha.typed.databinding.FragmentDetails1Binding
import com.alpha.typed.databinding.FragmentDetails7Binding
import com.alpha.typed.databinding.FragmentDetails8Binding
import com.alpha.typed.viewModels.DetailsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Details8.newInstance] factory method to
 * create an instance of this fragment.
 */
class Details8 : Fragment() ,EnterDetails.nextButtonListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDetails8Binding
    val sharedDetailsViewModel:DetailsViewModel by activityViewModels()
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
        binding = FragmentDetails8Binding.inflate(inflater,container,false)
        binding.doseIntegral.maxValue=150
        binding.doseIntegral.minValue=0
        binding.doseDecimal.maxValue=9
        binding.doseDecimal.minValue=0

        sharedDetailsViewModel.insulinDoses.observe(viewLifecycleOwner,{
            binding.doseText.setText(it)
        })

        binding.doseIntegral.setOnValueChangedListener{np,old,new->
            sharedDetailsViewModel.setDI(new.toString())
        }

        binding.doseDecimal.setOnValueChangedListener{np,old,new->
            sharedDetailsViewModel.setDD(new.toString())
        }
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
            Details8().apply {
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
        var doseI=binding.doseIntegral.value.toString()
        var doseD=binding.doseDecimal.value.toString()
        var result=doseI+"."+doseD
        if(result=="0.0"){
            Toast.makeText(requireActivity(),"Value cannot be zero!!", Toast.LENGTH_LONG)
        }
        else{
            sharedDetailsViewModel.setInsulinDoses(result)
            findNavController().navigate(R.id.action_details8_to_details9)
        }
    }
}