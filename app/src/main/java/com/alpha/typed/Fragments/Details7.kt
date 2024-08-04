package com.alpha.typed.Fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.Classes.Constants
import com.alpha.typed.R
import com.alpha.typed.databinding.FragmentDetails1Binding
import com.alpha.typed.databinding.FragmentDetails6Binding
import com.alpha.typed.databinding.FragmentDetails7Binding
import com.alpha.typed.viewModels.DetailsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Details7.newInstance] factory method to
 * create an instance of this fragment.
 */
class Details7 : Fragment() ,EnterDetails.nextButtonListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDetails7Binding
    val sharedDetailsViewModel: DetailsViewModel by activityViewModels()
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
        binding = FragmentDetails7Binding.inflate(inflater,container,false)
        binding.cm.maxValue=250
        binding.cm.minValue=55
        binding.mm.maxValue=9
        binding.mm.minValue=0

        sharedDetailsViewModel.hs.observe(viewLifecycleOwner,{
            binding.heightText.setText(it)
        })

        binding.cm.setOnValueChangedListener{np,old,new->
            sharedDetailsViewModel.setCm(new.toString())
        }

        binding.mm.setOnValueChangedListener{np,old,new->
            sharedDetailsViewModel.setMm(new.toString())
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
            Details7().apply {
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
        var cm=binding.cm.value.toString()
        var mm=binding.mm.value.toString()
        var result=cm+"."+mm
        sharedDetailsViewModel.setHeight(result)
        findNavController().navigate(R.id.action_details7_to_details8)

    }
}