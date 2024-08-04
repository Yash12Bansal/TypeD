package com.alpha.typed.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alpha.typed.Activities.EnterDetails
import com.alpha.typed.R
import com.alpha.typed.databinding.FragmentDetails9Binding
import com.alpha.typed.viewModels.DetailsViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Details9.newInstance] factory method to
 * create an instance of this fragment.
 */
class Details9 : Fragment() ,EnterDetails.nextButtonListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDetails9Binding
    val sharedDetailsViewModel:DetailsViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity.let {
            (it as EnterDetails).obj=this
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetails9Binding.inflate(inflater,container,false)
        binding.icrKnown.setOnCheckedChangeListener{group: RadioGroup, checkedId: Int   ->
            when (checkedId) {
                binding.yes.id -> {
                    binding.icrValues.visibility=View.VISIBLE
                }
                binding.no.id-> {
                    binding.icrValues.visibility=View.GONE

                }
            }
        };
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Details9().apply {
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
        if(binding.icrKnown.checkedRadioButtonId==binding.yes.id){
            if(binding.breakfastIcrText.text.toString().isEmpty() ||
                binding.lunchIcrText.text.toString().isEmpty() ||
                binding.snackIcrText.text.toString().isEmpty() ||
                binding.dinnerIcrText.text.toString().isEmpty()
                ){
                Toast.makeText(requireContext(), "Fill all the values!!", Toast.LENGTH_SHORT).show()
            }
            else{
                sharedDetailsViewModel.setBreakfastIcr(binding.breakfastIcrText.text.toString())
                sharedDetailsViewModel.setLunchIcr(binding.lunchIcrText.text.toString())
                sharedDetailsViewModel.setSnackIcr(binding.snackIcrText.text.toString())
                sharedDetailsViewModel.setDinnerIcr(binding.dinnerIcrText.text.toString())
                findNavController().navigate(com.alpha.typed.R.id.action_details9_to_details10)
            }
        }
        else{
            findNavController().navigate(com.alpha.typed.R.id.action_details9_to_details10)
        }

//        if (binding.email?.getText().toString().isEmpty()) {
//            Toast.makeText(requireContext(), "Doctor's email required!!", Toast.LENGTH_SHORT).show()
//            binding.doctorEmailCard.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.shake))
//        } else {
//
//            sharedDetailsViewModel.setDoctorEmail( binding.email.text.toString())
//        }

    }
}