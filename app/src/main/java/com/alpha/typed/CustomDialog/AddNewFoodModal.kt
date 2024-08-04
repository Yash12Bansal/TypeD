package com.alpha.typed.CustomDialog

import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.alpha.typed.Activities.FoodAdd
import com.alpha.typed.Classes.Constants
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Models.NewExerciseRequest
import com.alpha.typed.R
import com.alpha.typed.RetroInterfaces.NewFoodRequest
import com.alpha.typed.Utils.Loading
//import com.alpha.typed.RetroInterfaces.NewFoodRequest
import com.alpha.typed.databinding.FragmentAddNewFoodModalListDialogItemBinding
import com.alpha.typed.databinding.FragmentAddNewFoodModalListDialogBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Customize parameter argument names
//const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    AddNewFoodModal.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */

class AddNewFoodModal(var loading: Loading<FoodAdd>) : BottomSheetDialogFragment() {

    private var _binding: FragmentAddNewFoodModalListDialogBinding? = null
    lateinit var newFoodRequest: com.alpha.typed.Models.NewFoodRequest
    lateinit var api : NewFoodRequest
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddNewFoodModalListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<RecyclerView>(R.id.list)?.layoutManager =
            LinearLayoutManager(context)
        activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
            arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }
        api = RetrofitHelper.getInstance().create(com.alpha.typed.RetroInterfaces.NewFoodRequest::class.java)
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        var key=preferences.getString(Constants.KEY,null)?:"NULL"

        binding.rotateloading.visibility=View.INVISIBLE
        binding.submitButton.setOnClickListener {
            loading.startLoading()
            binding.rotateloading.visibility = View.VISIBLE
            binding.textSubmit.text = ""
            binding.submitButton.setClickable(false)
            if (binding.newFoodName.text.toString() != "") {
                newFoodRequest =
                    com.alpha.typed.Models.NewFoodRequest(binding.newFoodName.text.toString(),key)
                api.postFoodRequest(newFoodRequest)?.enqueue(object :
                    Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        loading.stopLoading()
                        binding.rotateloading.visibility = View.INVISIBLE
                        binding.textSubmit.text = "Submit"
                        binding.submitButton.setClickable(true)
                        binding.newFoodName.setText("")
                        Toast.makeText(
                            requireActivity(),
                            "Request sent succesfully.",
                            Toast.LENGTH_LONG
                        )

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            requireActivity(), "Could not send the request.\nTry again.",
                            Toast.LENGTH_LONG
                        )

                    }
                })
            } else {
                Toast.makeText(requireActivity(), "Write the exercise name", Toast.LENGTH_LONG)
            }
        }

    }

    private inner class ViewHolder internal constructor(binding: FragmentAddNewFoodModalListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal val text: TextView = binding.text
    }

    private inner class ItemAdapter internal constructor(private val mItemCount: Int) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentAddNewFoodModalListDialogItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = position.toString()
        }

        override fun getItemCount(): Int {
            return mItemCount
        }
    }

    companion object {

        // TODO: Customize parameters
//        fun newInstance(itemCount: Int): AddNewFoodModal =
//            AddNewFoodModal().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_ITEM_COUNT, itemCount)
//                }
//            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}