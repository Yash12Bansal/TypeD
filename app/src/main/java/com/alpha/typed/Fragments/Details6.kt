package com.alpha.typed.Fragments

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.Classes.Constants
import com.alpha.typed.R
import com.alpha.typed.databinding.FragmentDetails1Binding
import com.alpha.typed.databinding.FragmentDetails5Binding
import com.alpha.typed.databinding.FragmentDetails6Binding
import com.alpha.typed.viewModels.DetailsViewModel
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Details6.newInstance] factory method to
 * create an instance of this fragment.
 */
class Details6 : Fragment(),EnterDetails.nextButtonListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDetails6Binding
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
         binding = FragmentDetails6Binding.inflate(inflater,container,false)
        binding.kg.maxValue=150
        binding.kg.minValue=5
        binding.gm.maxValue=9
        binding.gm.minValue=0

        sharedDetailsViewModel.ws.observe(viewLifecycleOwner,{
            binding.weightText.setText(it)
        })

        binding.kg.setOnValueChangedListener{np,old,new->
            sharedDetailsViewModel.setKg(new.toString())
        }

        binding.gm.setOnValueChangedListener{np,old,new->
            sharedDetailsViewModel.setGm(new.toString())
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
            Details6().apply {
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
        var kg=binding.kg.value.toString()
        var gm=binding.gm.value.toString()
        var result=kg+"."+gm
        sharedDetailsViewModel.setWeight(result)
        findNavController().navigate(R.id.action_details6_to_details7)

    }
}