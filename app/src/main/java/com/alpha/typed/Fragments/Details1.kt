package com.alpha.typed.Fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.Classes.Constants.NAME
import com.alpha.typed.Classes.Constants.PERSON_NAME
import com.alpha.typed.R
import com.alpha.typed.databinding.FragmentDetails1Binding
import com.alpha.typed.viewModels.DetailsViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Details1 : Fragment(),EnterDetails.nextButtonListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var binding:FragmentDetails1Binding
    private var param2: String? = null
    val sharedDetailsViewModel :DetailsViewModel by activityViewModels()

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
        binding = FragmentDetails1Binding.inflate(inflater,container,false)
        return binding.root

    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Details1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity.let {
            (it as EnterDetails).obj=this
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

        if (binding.name!!.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Patient's name required!!", Toast.LENGTH_SHORT).show()
            binding.nameCard.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.shake))
        }
        else {
            sharedDetailsViewModel.setName(binding.name.text.toString()?:"NULL")
            findNavController().navigate(R.id.action_details1_to_details2)
        }

    }
}