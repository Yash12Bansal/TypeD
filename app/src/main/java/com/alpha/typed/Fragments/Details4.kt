package com.alpha.typed.Fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.Classes.Constants
import com.alpha.typed.R
import com.alpha.typed.databinding.FragmentDetails1Binding
import com.alpha.typed.databinding.FragmentDetails3Binding
import com.alpha.typed.databinding.FragmentDetails4Binding
import com.alpha.typed.viewModels.DetailsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Details4.newInstance] factory method to
 * create an instance of this fragment.
 */
class Details4 : Fragment() ,EnterDetails.nextButtonListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDetails4Binding
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
         binding = FragmentDetails4Binding.inflate(inflater,container,false)
        val spinnerAdapter = ArrayAdapter<String>(requireContext() ,android. R.layout.simple_spinner_item, android.R.id.text1)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner?.setAdapter(spinnerAdapter)
        spinnerAdapter.add("Male")
        spinnerAdapter.add("Female")
        spinnerAdapter.add("Other")
        spinnerAdapter.notifyDataSetChanged()
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
            Details4().apply {
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
        if (binding.genderSpinner?.getSelectedItem().toString()
                .isEmpty() || binding.genderSpinner?.getSelectedItem().toString() == "Select"
        ) {
            Toast.makeText(requireContext(), "Select the gender!!", Toast.LENGTH_SHORT).show()

        } else {
            sharedDetailsViewModel.setSex(binding.genderSpinner?.getSelectedItem().toString())
            findNavController().navigate(R.id.action_details4_to_details5)
        }

    }
}