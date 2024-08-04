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
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.Classes.Constants
import com.alpha.typed.R
import com.alpha.typed.databinding.FragmentDetails1Binding
import com.alpha.typed.databinding.FragmentDetails2Binding
import com.alpha.typed.databinding.FragmentDetails5Binding
import com.alpha.typed.viewModels.DetailsViewModel
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Details5.newInstance] factory method to
 * create an instance of this fragment.
 */
class Details5 : Fragment(),EnterDetails.nextButtonListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDetails5Binding
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
         binding = FragmentDetails5Binding.inflate(layoutInflater,container,false)
        val mcurrenttime = Calendar.getInstance()
        val year = mcurrenttime[Calendar.YEAR]
        val month = mcurrenttime[Calendar.MONTH]
        val day = mcurrenttime[Calendar.DAY_OF_MONTH]
        val default_date = year.toString() + "-" + (month + 1) + "-" + day
        val hour = mcurrenttime[Calendar.HOUR_OF_DAY]
        val minute = mcurrenttime[Calendar.MINUTE]
        binding.diagnosis.setText(default_date)
        binding.diagnosis!!.setOnClickListener(View.OnClickListener {
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog,
                { datePicker, year, month, day ->
                    var final_date = year.toString() + "-" + (month + 1) + "-" + day.toString()
                    binding.diagnosis.setText(final_date)
                }, year, month, day
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        })
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
            Details5().apply {
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
        sharedDetailsViewModel.setYod(binding.diagnosis.text.toString())
        findNavController().navigate(R.id.action_details5_to_details6)

    }
}