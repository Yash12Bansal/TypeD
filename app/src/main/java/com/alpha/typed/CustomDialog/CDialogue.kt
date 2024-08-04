package com.alpha.typed.CustomDialog
import com.alpha.typed.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
import com.alpha.typed.Classes.Constants.KEY
import com.alpha.typed.Fragments.Food
import com.alpha.typed.Navigation_drawer.MainScreen


class CDialogue(var c: Activity) : Dialog(c), View.OnClickListener {
    var contex: Context? = null
//    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
//    var myRef: DatabaseReference = database.getReference("food_entry")
    var d: Dialog? = null
    var yes: Button? = null
    var no: Button? = null
    var editText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cdialogue)
        editText = findViewById<View>(R.id.edit) as EditText
        yes = findViewById<View>(R.id.btn_yes) as Button
        no = findViewById<View>(R.id.btn_no) as Button
        yes!!.setOnClickListener(this)
        no!!.setOnClickListener(this)
//        contex = Food.food_context
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager =
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_yes -> {
                if (!isNetworkAvailable) {
                    Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getContext(), "Done..!!", Toast.LENGTH_SHORT).show()
                    val time = System.currentTimeMillis()
                    val preferences = PreferenceManager.getDefaultSharedPreferences(getContext())
                    val key = preferences.getString(KEY, null)
                    if (editText!!.text == null || editText!!.text.toString().length == 0) {
                        Toast.makeText(getContext(), "No food suggested!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
//                        myRef.child(key!!).child(time.toString() + "")
//                            .setValue(editText!!.text.toString())
//                        context!!.startActivity(
//                            Intent(
//                                context,
//                                MainScreen::class.java
//                            )
//                        )
                    }
                }
                dismiss()
            }
            R.id.btn_no -> dismiss()
            else -> dismiss()
        }
    }
}