package com.alpha.typed.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.alpha.typed.R
import com.alpha.typed.databinding.ActivityEnterDetailsBinding
import com.alpha.typed.databinding.ActivityLoginBinding

class EnterDetails : AppCompatActivity() {
    interface nextButtonListener{
        fun onNextClick()
    }
    var obj:nextButtonListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityEnterDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.next.setOnClickListener {
            obj?.onNextClick()
        }


////        supportActionBar!!.hide()
//        name = findViewById<View>(R.id.name) as EditText
//        age = findViewById(R.id.age)
//        mobile = findViewById(R.id.mobile_no)
//        spinner = findViewById(R.id.gender_spinner)
//        submit = findViewById(R.id.submit)
//        val spinnerAdapter =
//            ArrayAdapter<String>(this@EnterDetails,android. R.layout.simple_spinner_item, android.R.id.text1)
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner?.setAdapter(spinnerAdapter)
//        spinnerAdapter.add("Male")
//        spinnerAdapter.add("Female")
//        spinnerAdapter.add("Other")
//        spinnerAdapter.notifyDataSetChanged()
//        val preferences = PreferenceManager.getDefaultSharedPreferences(this@EnterDetails)
////        name!!.setText(preferences.getString(PERSON_NAME, null))
//        submit?.setOnClickListener(View.OnClickListener {
//            val b = AlertDialog.Builder(this@EnterDetails)
//            b.setTitle("Alert")
//            b.setMessage("Are you sure you want to submit ?")
//            b.setPositiveButton(
//                "Yes"
//            ) { dialogInterface, index ->
//                if (name!!.text.toString().isEmpty()) {
//                    Toast.makeText(this@EnterDetails, "Enter name", Toast.LENGTH_SHORT).show()
//                } else if (spinner?.getSelectedItem().toString()
//                        .isEmpty() || spinner?.getSelectedItem().toString() == "select"
//                ) {
//                    Toast.makeText(this@EnterDetails, "Enter gender", Toast.LENGTH_SHORT).show()
//                } else if (mobile?.getText().toString().isEmpty()) {
//                    Toast.makeText(this@EnterDetails, "Enter mobile number", Toast.LENGTH_SHORT)
//                        .show()
//                } else if (age?.getText().toString().isEmpty()) {
//                    Toast.makeText(this@EnterDetails, "Enter age", Toast.LENGTH_SHORT).show()
//                } else {
//                    val preferences =
//                        PreferenceManager.getDefaultSharedPreferences(this@EnterDetails)
////                    val key = preferences.getString(KEY, null)
////                    if (key == null) {
////                        Toast.makeText(
////                            this@EnterDetails,
////                            "Something went wrong!",
////                            Toast.LENGTH_SHORT
////                        ).show()
////                    } else {
////                        val time = System.currentTimeMillis()
////                        val editor = preferences.edit()
////
////                        editor.putString(PERSON_NAME, name!!.text.toString())
////                        editor.putString(GENDER, spinner?.getSelectedItem().toString())
////                        editor.apply()
////                        myRef.child(key).child("NAME").setValue(name!!.text.toString())
////                        myRef.child(key).child("AGE").setValue(age?.getText()?.toString())
////                        myRef.child(key).child("GENDER").setValue(spinner?.getSelectedItem())
////                        myRef.child(key).child("MOBILENO").setValue(mobile?.getText()?.toString())
////                        startActivity(Intent(this@EnterDetails, MainScreen::class.java).addFlags(
////                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
////                        this@EnterDetails.finish()
////                    }
//                }
//                dialogInterface.cancel()
//            }
//            b.setNegativeButton(
//                "No"
//            ) { dialogInterface, i -> dialogInterface.cancel() }
//            b.show()
//        })
    }
    private fun signOut() {
//        Login.mGoogleSignInClient?.signOut()
//            ?.addOnCompleteListener(this, object : OnCompleteListener<Void?> {
//                override fun onComplete(task: Task<Void?>) {
//                    this@EnterDetails.finish()
//                }
//            })
    }

//    override fun onBackPressed() {
//        val b = AlertDialog.Builder(this@EnterDetails)
//        b.setTitle("Alert")
//        b.setMessage("Do you want to leave without signing up?")
//        b.setPositiveButton(
//            "Yes"
//        ) { dialogInterface, index ->
////            FirebaseAuth.getInstance().signOut()
////            signOut()
////            startActivity(Intent(this@EnterDetails , Login::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
////            dialogInterface.cancel()
//////            this@EnterDetails.finish()
////
////            dialogInterface.cancel()
//        }
//        b.setNegativeButton(
//            "No"
//        ) { dialogInterface, i -> dialogInterface.cancel() }
//        b.show()
//
//    }
}