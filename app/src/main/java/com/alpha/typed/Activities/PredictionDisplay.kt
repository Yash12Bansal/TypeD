package com.alpha.typed.Activities

//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.victor.loading.rotate.RotateLoading
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.alpha.typed.Classes.Constants.CALORIES
import com.alpha.typed.Classes.Constants.CARBS
import com.alpha.typed.Classes.Constants.CURRENT_BG
import com.alpha.typed.Classes.Constants.CURRENT_DATE
import com.alpha.typed.Classes.Constants.CURRENT_TIME
import com.alpha.typed.Classes.Constants.DB_CATEGORY_ENTERED
import com.alpha.typed.Classes.Constants.DIRECT_INSULIN_SCREEN
import com.alpha.typed.Classes.Constants.FAT
import com.alpha.typed.Classes.Constants.FIXED_BG
import com.alpha.typed.Classes.Constants.FIXED_EXERCISE_TIME
import com.alpha.typed.Classes.Constants.FIXED_INSULIN
import com.alpha.typed.Classes.Constants.FIXED_WORD_EXERCISE
import com.alpha.typed.Classes.Constants.FOOD_DB_NAME
import com.alpha.typed.Classes.Constants.FOOD_DB_QUANTITY
import com.alpha.typed.Classes.Constants.FOOD_DB_TIME_STAMP
import com.alpha.typed.Classes.Constants.FOOD_HELPER_DB_ID
import com.alpha.typed.Classes.Constants.FOOD_HELPER_DB_NAME
import com.alpha.typed.Classes.Constants.FOOD_HELPER_DB_NAME_LOG
import com.alpha.typed.Classes.Constants.KEEP_COUNT_BG
import com.alpha.typed.Classes.Constants.KEEP_COUNT_EXERCISE
import com.alpha.typed.Classes.Constants.KEEP_COUNT_INSULIN
import com.alpha.typed.Classes.Constants.KEY
import com.alpha.typed.Classes.Constants.PREDICT_SCREEN_CATEGORY
import com.alpha.typed.Classes.Constants.PROTEIN
import com.alpha.typed.Classes.Constants.STORED_DATE
import com.alpha.typed.Classes.Constants.TIME_IN_MIN_INSULIN
import com.alpha.typed.Classes.Constants.TOTAL_CARBS_FOR_PREDICTION
import com.alpha.typed.Classes.Constants.USER_DATE_ENTERED
import com.alpha.typed.Classes.Constants.USER_TIME_ENTERED
import com.alpha.typed.Classes.FirebaseUserInfo
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Models.AcceptedAPI
import com.alpha.typed.Models.FoodEntry
import com.alpha.typed.Models.PredictionExtraDetails
import com.alpha.typed.Models.PredictionTrainedParamsFetch
import com.alpha.typed.Models.PredictionValuesStatus
import com.alpha.typed.Models.RejectedAPI
import com.alpha.typed.Navigation_drawer.MainScreen
import com.alpha.typed.R
import com.alpha.typed.RetroInterfaces.FoodInterface
import com.alpha.typed.RetroInterfaces.Prediction
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityMainScreenBinding
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class PredictionDisplay : AppCompatActivity() {
//    var database2: FirebaseDatabase = FirebaseDatabase.getInstance()
//    var myRef2: DatabaseReference = database2.getReference("exercise_entry/Prediction_values")
//    var myRef3: DatabaseReference =
//        database2.getReference("exercise_entry/Accepted_Prediction_values")
//    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
//    var foodref: DatabaseReference = database.getReference("food")
//    var preref: DatabaseReference = database.getReference("exercise_entry/Prediction")
    var avgbreakfast: Double = 1.0
    var avglunch: Double = 1.0
    var avgsnack: Double = 1.0
    var avgdinner: Double = 1.0
    lateinit var apiPred:Prediction
    lateinit var apiFood:FoodInterface
    var myicr: Double = 1.0
    var myisf: Double = 1.0
    var breakfast_icr: Double = 1.0
    var lunch_icr: Double = 1.0
    var dinner_icr: Double = 1.0
    var snack_icr: Double = 1.0
    var breakfast_isf: Double = 1.0
    var lunch_isf: Double = 1.0
    var snack_isf: Double = 1.0
    var dinner_isf: Double = 1.0
    var myprev_dose:Double=1.0
    var curr_bg: String? = null
    var total_carbs: String? = null
    var time_prev_dose_in_milli: String = "0"
    var division_factor: Double = 1.0
    var predicted_value_show: TextView? = null
    var main_text: TextView? = null
    var accept: MaterialCardView? = null
    var reject: MaterialCardView? = null
    var time_in_min: String? = null
    var id = 0
    var dataBaseFoodHelperLog: DataBaseFoodHelperLog? = null
    var dataBaseFoodHelper: DataBaseFoodHelper? = null
    var time_stamp: Long = 0
    var food_name: String? = null
    var date: String? = null
    var food_carbs: String? = null
    var quantity: String? = null
    var time: String? = null
    var category: String? = null
    var prev_date: String? = null
    var prev_time: String? = null
    var rotateLoading: ProgressBar? = null
    var went_in = false
    var pre_category: String? = null
    lateinit var api : Prediction
    lateinit var loading: Loading<PredictionDisplay>
    lateinit var rotateloading:View
    lateinit var screen:View
    lateinit var api2:Prediction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction_display)
        rotateloading=findViewById(R.id.rotateloading)
        rotateloading.visibility=View.INVISIBLE
        screen = findViewById(R.id.screen)
        loading= Loading<PredictionDisplay>(this,rotateloading,screen)

        main_text = findViewById(R.id.description_all)
        apiPred= RetrofitHelper.getInstance().create(Prediction::class.java)
        apiFood=RetrofitHelper.getInstance().create(FoodInterface::class.java)
        accept = findViewById(R.id.accept)
        reject = findViewById(R.id.reject)
        went_in = false
        dataBaseFoodHelperLog = DataBaseFoodHelperLog(this@PredictionDisplay)
        dataBaseFoodHelper = DataBaseFoodHelper(this@PredictionDisplay)
//        rotateLoading = findViewById<View>(R.id.rotateloading) as ProgressBar
        predicted_value_show = findViewById(R.id.value)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
        curr_bg = preferences.getString(CURRENT_BG, null)
        total_carbs = preferences.getFloat(TOTAL_CARBS_FOR_PREDICTION, 0f).toString() + ""
        time_in_min = preferences.getString(TIME_IN_MIN_INSULIN, null)
        pre_category = preferences.getString(PREDICT_SCREEN_CATEGORY, null)
//        Toast.makeText(
//            this@PredictionDisplay,
//            "Error in Prediction Display $pre_category",
//            Toast.LENGTH_SHORT
//        ).show()

        loading.startLoading()
        prev_date = preferences.getString(CURRENT_DATE, null)
        prev_time = preferences.getString(CURRENT_TIME, null)
//        rotateLoading?.visibility=View.INVISIBLE
        if (!isNetworkAvailable) {
            Toast.makeText(this@PredictionDisplay, "No internet connection", Toast.LENGTH_SHORT).show()
            loading.stopLoading()
        } else {
            try {
                CheckExist()
            } catch (e: Exception) {
                loading.stopLoading()
                Toast.makeText(
                    this@PredictionDisplay,
                    "Error in Prediction Display",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        accept!!.setOnClickListener(View.OnClickListener {
            if (!isNetworkAvailable) {
                Toast.makeText(this@PredictionDisplay, "No internet connection", Toast.LENGTH_SHORT)
                    .show()
            } else {
                try {
                    val alertDialog = AlertDialog.Builder(
                        this, R.style.CustomAlertDialog
                    ).create()
                    alertDialog.setCancelable(false)
                    val layoutInflater = LayoutInflater.from(this)
                    val convertView: View =
                        layoutInflater.inflate(R.layout.confirmation_dialogs_layout, null, true)
                    alertDialog.setView(convertView)

                    var yes: CardView = convertView.findViewById(R.id.yes)
                    var no: MaterialCardView = convertView.findViewById(R.id.no)
                    var confirmText: TextView = convertView.findViewById(R.id.yes_text)
                    confirmText.setText("Confirm")
                    var msg: TextView = convertView.findViewById(R.id.message)
                    msg.setText("Click \"Confirm\" to accept the recommended dose")
                    yes.setOnClickListener {
                        loading.startLoading()
                        addFoodAndPrediction()
                        val preferences2 =
                            PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
                        val key = preferences2.getString(KEY, null)
                        val time = System.currentTimeMillis()
                        val mcurrenttime = Calendar.getInstance()

                        val year = mcurrenttime[Calendar.YEAR]
                        val month = mcurrenttime[Calendar.MONTH]
                        val day = mcurrenttime[Calendar.DAY_OF_MONTH]
                        val current_date = year.toString() + "-" + (month + 1) + "-" + day
                        val current_time = Calendar.getInstance()
                        val hour = current_time[Calendar.HOUR_OF_DAY]
                        val minute = current_time[Calendar.MINUTE]
                        val current__time = "$hour:$minute"

                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        var finalDateN = formatter.parse(prev_date?.toString())

                        var pred = AcceptedAPI(
                            key ?: "NULL",
                            predicted_value_show?.getText().toString().toDouble(),
                            finalDateN,
                            prev_time.toString(),
                            "Accepted",
                            predicted_value_show?.text.toString().toDouble()?:0.0,
                            System.currentTimeMillis().toString()
                        )
                        apiPred.acceptPrediction(pred)?.enqueue(object :
                            Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                var str = response.body()?.string()
                                val jsonObject: JsonObject =
                                    JsonParser().parse(str).getAsJsonObject()
                                var xx = jsonObject.get("msg")
                                loading.stopLoading()
                                Toast.makeText(
                                    this@PredictionDisplay,
                                    "You have successfully accepted the prediction.",
                                    Toast.LENGTH_LONG
                                ).show()
                                if ((jsonObject.get("msg")
                                        .toString()).equals("\"Nahi tha re User\"")
                                ) {
//                                        loading.stopLoading()
                                } else {
                                    //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        requireContext().finish()

                                }

                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                loading.stopLoading()
                                Toast.makeText(
                                    this@PredictionDisplay,
                                    "Could not add your accepted dose. Try to accept it again.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })


//                        myRef3.child(key!!).child(time.toString() + "").child("AMOUNT")
//                            .setValue(predicted_value_show!!.getText().toString())
//                        myRef3.child(key!!).child(time.toString() + "").child("CURRENT_TIME")
//                            .setValue(current__time)
//                        myRef3.child(key!!).child(time.toString() + "").child("CURRENT_DATE")
//                            .setValue(current_date)
//                        myRef3.child(key!!).child(time.toString() + "").child("DATE")
//                            .setValue(prev_date)
//                        myRef3.child(key!!).child(time.toString() + "").child("TIME")
//                            .setValue(prev_time)
//                        myRef3.child(key!!).child(time.toString() + "").child("STATUS")
//                            .setValue("Accept")
//                        myRef2.child(key!!).child("INSULIN_DOSE")
//                            .setValue(predicted_value_show!!.getText().toString())
//                        myRef2.child(key!!).child("PREV_INSULIN_TIME").setValue(time_in_min)
                        val getpreferences =
                            PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
                        var count_here = getpreferences.getInt(KEEP_COUNT_INSULIN, 0)
                        val preferences_contents =
                            PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
                        val editor = preferences_contents.edit()
                        //
                        count_here++
                        editor.putInt(KEEP_COUNT_INSULIN, count_here)
                        editor.putString(
                            FIXED_INSULIN + count_here,
                            predicted_value_show!!.getText().toString()
                        )

                        editor.remove(PREDICT_SCREEN_CATEGORY)
                        editor.remove(CURRENT_BG)
                        editor.remove(TIME_IN_MIN_INSULIN)
                        editor.remove(TOTAL_CARBS_FOR_PREDICTION)
                        editor.remove(CURRENT_DATE)
                        editor.remove(CURRENT_TIME)
                        editor.apply()
                        editor.commit()
                        Toast.makeText(
                            this@PredictionDisplay,
                            "Insulin Accepted",
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent()
                        i.setClass(this@PredictionDisplay, MainScreen::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                        i.putExtra(DIRECT_INSULIN_SCREEN, "insulin")

                        startActivity(i)
                        this@PredictionDisplay.finish()

//                        finish()
//                        startActivity(i)
                        alertDialog.cancel()
                    }
                    no.setOnClickListener {
                        alertDialog.cancel()
                    }
                    alertDialog.show()

//                    val b = AlertDialog.Builder(this@PredictionDisplay)
//                    b.setTitle("Alert")
//                    b.setMessage("Click \"Confirm\" to accept the recommended dose")
//                    b.setPositiveButton(
//                        "Yes"
//                    ) { dialogInterface, index ->
//                        AddInFirebase()
//                        val preferences2 =
//                            PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
//                        val key = preferences2.getString(KEY, null)
//                        val time = System.currentTimeMillis()
//                        val mcurrenttime = Calendar.getInstance()
//
//                        val year = mcurrenttime[Calendar.YEAR]
//                        val month = mcurrenttime[Calendar.MONTH]
//                        val day = mcurrenttime[Calendar.DAY_OF_MONTH]
//                        val current_date = year.toString() + "-" + (month + 1) + "-" + day
//                        val current_time = Calendar.getInstance()
//                        val hour = current_time[Calendar.HOUR_OF_DAY]
//                        val minute = current_time[Calendar.MINUTE]
//                        val current__time = "$hour:$minute"
//
//                        val formatter = SimpleDateFormat("yyyy-MM-dd")
//                        var finalDateN = formatter.parse(prev_date?.toString())
//
//                        var pred= PredictionValuesStatus(key?:"NULL",predicted_value_show?.getText().toString().toDouble(),finalDateN,prev_time.toString(),"Accept")
//                        apiPred.addPrediction(pred)?.enqueue(object :
//                            Callback<ResponseBody> {
//                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                                var str=response.body()?.string()
//                                val jsonObject: JsonObject =
//                                    JsonParser().parse(str).getAsJsonObject()
//                                var xx=jsonObject.get("msg")
//                                Toast.makeText(this@PredictionDisplay,""+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
//                                if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){
////                                        loading.stopLoading()
//                                }
//                                else{
//                                    //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
////                                        requireContext().finish()
//
//                                }
//
//                            }
//
//                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                                Log.e("gee",""+t.stackTrace)
////                    Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()
//
//                                Log.e("gee","onFailureeeee"+t.message)
//
//                                // handle the failure
//                            }
//                        })
//
//
////                        myRef3.child(key!!).child(time.toString() + "").child("AMOUNT")
////                            .setValue(predicted_value_show!!.getText().toString())
////                        myRef3.child(key!!).child(time.toString() + "").child("CURRENT_TIME")
////                            .setValue(current__time)
////                        myRef3.child(key!!).child(time.toString() + "").child("CURRENT_DATE")
////                            .setValue(current_date)
////                        myRef3.child(key!!).child(time.toString() + "").child("DATE")
////                            .setValue(prev_date)
////                        myRef3.child(key!!).child(time.toString() + "").child("TIME")
////                            .setValue(prev_time)
////                        myRef3.child(key!!).child(time.toString() + "").child("STATUS")
////                            .setValue("Accept")
////                        myRef2.child(key!!).child("INSULIN_DOSE")
////                            .setValue(predicted_value_show!!.getText().toString())
////                        myRef2.child(key!!).child("PREV_INSULIN_TIME").setValue(time_in_min)
//                        val getpreferences =
//                            PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
//                        var count_here = getpreferences.getInt(KEEP_COUNT_INSULIN, 0)
//                        val preferences_contents =
//                            PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
//                        val editor = preferences_contents.edit()
//                        //
//                        count_here++
//                        editor.putInt(KEEP_COUNT_INSULIN, count_here)
//                        editor.putString(
//                            FIXED_INSULIN + count_here,
//                            predicted_value_show!!.getText().toString()
//                        )
//
//                        editor.remove(PREDICT_SCREEN_CATEGORY)
//                        editor.remove(CURRENT_BG)
//                        editor.remove(TIME_IN_MIN_INSULIN)
//                        editor.remove(TOTAL_CARBS_FOR_PREDICTION)
//                        editor.remove(CURRENT_DATE)
//                        editor.remove(CURRENT_TIME)
//                        editor.apply()
//                        editor.commit()
//                        Toast.makeText(
//                            this@PredictionDisplay,
//                            "Insulin Accepted",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        val i = Intent()
//                        i.setClass(this@PredictionDisplay, MainScreen::class.java)
//                        finish()
//                        startActivity(i)
//                        dialogInterface.cancel()
//                    }
//                    b.setNegativeButton(
//                        "No"
//                    ) { dialogInterface, i -> dialogInterface.cancel() }
//                    b.show()
//                }

                }catch (e: Exception) {
                    loading.stopLoading()
                    Toast.makeText(this@PredictionDisplay, "Some unknown error occurred. Try again!", Toast.LENGTH_SHORT).show()
                }
            }
        })
        reject!!.setOnClickListener(View.OnClickListener {
            try {
                val alertDialog = AlertDialog.Builder(
                    this, R.style.CustomAlertDialog
                ).create()
                alertDialog.setCancelable(false)
                val layoutInflater = LayoutInflater.from(this)
                val convertView: View =
                    layoutInflater.inflate(R.layout.confirmation_dialogs_layout, null, true)
                alertDialog.setView(convertView)


                var yes: CardView = convertView.findViewById(R.id.yes)
                var no: MaterialCardView = convertView.findViewById(R.id.no)
                var confirmText: TextView = convertView.findViewById(R.id.yes_text)
                confirmText.setText("Confirm")
                var msg: TextView = convertView.findViewById(R.id.message)
                msg.setText("Click \"Confirm\" to reject the recommended dose")
                yes.setOnClickListener {
                    loading.startLoading()
                    addFoodAndPrediction()
//                    Toast.makeText(
//                        this@PredictionDisplay,
//                        "Insulin Rejected. \nAdd Insulin",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    val preferences2 =
                        PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
                    val key = preferences2.getString(KEY, null)
                    val time = System.currentTimeMillis()
                    val mcurrenttime = Calendar.getInstance()
                    val year = mcurrenttime[Calendar.YEAR]
                    val month = mcurrenttime[Calendar.MONTH]
                    val day = mcurrenttime[Calendar.DAY_OF_MONTH]
                    val current_date = year.toString() + "-" + (month + 1) + "-" + day
                    val current_time = Calendar.getInstance()
                    val hour = current_time[Calendar.HOUR_OF_DAY]
                    val minute = current_time[Calendar.MINUTE]
                    val current__time = "$hour:$minute"


                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    var finalDateN = formatter.parse(prev_date?.toString())

                    var pred = RejectedAPI(
                        key ?: "NULL",
                        predicted_value_show?.getText().toString().toDouble(),
                        finalDateN,
                        prev_time.toString(),
                        "Rejected"
                    )
                    apiPred.rejectPrediction(pred)?.enqueue(object :
                        Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            var str = response.body()?.string()
                            val jsonObject: JsonObject =
                                JsonParser().parse(str).getAsJsonObject()
                            var xx = jsonObject.get("msg")

                            loading.stopLoading()
                            Toast.makeText(
                                this@PredictionDisplay,
                                "Rejected dose recorded. We will try to increase the accuracy of the system.",
                                Toast.LENGTH_LONG
                            ).show()
                            if ((jsonObject.get("msg")
                                    .toString()).equals("\"Nahi tha re User\"")
                            ) {
//                                        loading.stopLoading()
                            } else {
                                //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        requireContext().finish()

                            }

                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            loading.stopLoading()
                            Toast.makeText(
                                this@PredictionDisplay,
                                "Error occured in rejecting the dose. Try to reject it again.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })



//                    myRef3.child(key!!).child(time.toString() + "").child("AMOUNT")
//                        .setValue(predicted_value_show!!.getText().toString())
//                    myRef3.child(key).child(time.toString() + "").child("CURRENT_TIME")
//                        .setValue(current__time)
//                    myRef3.child(key).child(time.toString() + "").child("CURRENT_DATE")
//                        .setValue(current_date)
//                    myRef3.child(key).child(time.toString() + "").child("DATE")
//                        .setValue(prev_date)
//                    myRef3.child(key).child(time.toString() + "").child("TIME")
//                        .setValue(prev_time)
//                    myRef3.child(key).child(time.toString() + "").child("STATUS")
//                        .setValue("Reject")

                    var options: ActivityOptions? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        options =
                            ActivityOptions.makeSceneTransitionAnimation(this@PredictionDisplay)
                    }
                    alertDialog.cancel()
                    val ii = Intent()
                    ii.setClass(this@PredictionDisplay, MainScreen::class.java)
                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    ii.putExtra(DIRECT_INSULIN_SCREEN, "insulin")

                    startActivity(ii, options!!.toBundle())
                    this@PredictionDisplay.finish()

//                    finish()

//                    startActivity(ii, options!!.toBundle())
                }
                no.setOnClickListener {
                    alertDialog.cancel()
                }
                alertDialog.show()

                /////////////////
//                val b = AlertDialog.Builder(this@PredictionDisplay)
//                b.setTitle("Alert")
//                b.setMessage("Are you sure you want to reject the recommended dose?")
//                b.setPositiveButton(
//                    "Yes"
//                ) { dialogInterface, index ->
//                    AddInFirebase()
//                    Toast.makeText(
//                        this@PredictionDisplay,
//                        "Insulin Rejected. \nAdd Insulin",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    val preferences2 =
//                        PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
//                    val key = preferences2.getString(KEY, null)
//                    val time = System.currentTimeMillis()
//                    val mcurrenttime = Calendar.getInstance()
//                    val year = mcurrenttime[Calendar.YEAR]
//                    val month = mcurrenttime[Calendar.MONTH]
//                    val day = mcurrenttime[Calendar.DAY_OF_MONTH]
//                    val current_date = year.toString() + "-" + (month + 1) + "-" + day
//                    val current_time = Calendar.getInstance()
//                    val hour = current_time[Calendar.HOUR_OF_DAY]
//                    val minute = current_time[Calendar.MINUTE]
//                    val current__time = "$hour:$minute"
////                    myRef3.child(key!!).child(time.toString() + "").child("AMOUNT")
////                        .setValue(predicted_value_show!!.getText().toString())
////                    myRef3.child(key).child(time.toString() + "").child("CURRENT_TIME")
////                        .setValue(current__time)
////                    myRef3.child(key).child(time.toString() + "").child("CURRENT_DATE")
////                        .setValue(current_date)
////                    myRef3.child(key).child(time.toString() + "").child("DATE")
////                        .setValue(prev_date)
////                    myRef3.child(key).child(time.toString() + "").child("TIME")
////                        .setValue(prev_time)
////                    myRef3.child(key).child(time.toString() + "").child("STATUS")
////                        .setValue("Reject")
//
//                    var options: ActivityOptions? = null
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        options =
//                            ActivityOptions.makeSceneTransitionAnimation(this@PredictionDisplay)
//                    }
//                    val ii = Intent()
//                    ii.setClass(this@PredictionDisplay, MainScreen::class.java)
//                    ii.putExtra(DIRECT_INSULIN_SCREEN, "insulin")
//                    finish()
//
//                    startActivity(ii, options!!.toBundle())
//                }
//                b.setNegativeButton(
//                    "No"
//                ) { dialogInterface, i -> dialogInterface.cancel() }
//                b.show()
            } catch (e: Exception) {
                loading.stopLoading()

                Toast.makeText(this@PredictionDisplay, "Some unknown error occurred. Try again!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun findValues() {

//        try {

        if (pre_category == "Breakfast") {
            myicr = breakfast_icr
            myisf = breakfast_isf
        }
        if (pre_category == "Lunch") {
            myicr = lunch_icr
            myisf = lunch_isf
        }
        if (pre_category == "Dinner") {
            myicr = dinner_icr
            myisf = dinner_isf
        }
        if (pre_category == "Snack") {
            myicr = snack_icr
            myisf = snack_isf
        }

    //            Toast.makeText(
//                this@PredictionDisplay,
//                "Inside q's try block $myicr ",
//                Toast.LENGTH_SHORT
//            ).show()
        fun String.fullTrim()=trim().replace("\uFEFF","")
//            myicr=myicr!!.fullTrim()
        total_carbs=total_carbs!!.fullTrim()
        curr_bg=curr_bg!!.fullTrim()
//            myisf=myisf!!.fullTrim()
        var t=System.currentTimeMillis()
        var x1 = total_carbs?.toDouble()?.div((myicr) !!.toDouble())
        var x2 = ((curr_bg)?.toDouble()?.minus(140.0))?.div((myisf)!!.toDouble())
//        var IOB:Double = myprev_dose*(1-((t - time_prev_dose_in_milli.toInt())/6.0))

        var ans = x1?.plus(x2!!)

//        var BOLUS=ans-IOB
//            Log.i("see_the_time", "$time_in_min g $time_prev_dose_in_milli")
        if (java.lang.Double.valueOf(myprev_dose) > 0 && java.lang.Double.valueOf(myprev_dose) != 0.0 && java.lang.Double.valueOf(
                time_prev_dose_in_milli
            ) > 0
        ) {

            if (division_factor == null) {
                division_factor = 4.0
            }
            //HERE PREVIOUSLY (java.lang.Double.valueOf(time_in_min) WAS USED INSTEAD OF t
            val IOB = java.lang.Double.valueOf(myprev_dose) * (1 - ((t - java.lang.Double.valueOf(time_prev_dose_in_milli)) / (division_factor!!.toInt() * 60 * 60 * 1000)))
            if (ans != null) {
                if (IOB > 0) {
                    ans -= IOB
                }
            }
        }
        var final_prediction = ans?.toInt()?.toDouble()
        Toast.makeText(
            this@PredictionDisplay,
            final_prediction.toString()+" Final value",
            Toast.LENGTH_SHORT
        ).show()

        val fractional = ans?.minus(final_prediction!!)
        if (final_prediction != null) {
            final_prediction = if (Math.abs(0.5 - fractional!!) <= 0.2) {
                final_prediction + 0.5
            } else {
                Math.round(ans!!).toInt().toDouble()
            }
        }
//            Log.i("point5check", "$final_prediction g $fractional")
//            Log.i(
//                "see_data",
//                "$total_carbs g $myicr g $curr_bg g $myisf g $val1 gg $val2"
//            )
        if (final_prediction != null) {
            if (final_prediction < 0) {
                predicted_value_show?.setText(0.toString()+" ")
//                    Toast.makeText(
//                        this@PredictionDisplay,
//                        "Inside find Values  fgdjf",
//                        Toast.LENGTH_SHORT
//                    ).show()

            } else {
                predicted_value_show?.setText("$final_prediction ")
                var carb_check: Double = 1.0
                if (pre_category == "Breakfast") {
                    carb_check = avgbreakfast
                } else if (pre_category == "Lunch") {
                    carb_check = avglunch
                } else if(pre_category=="Snack"){
                    carb_check=avgsnack
                } else if (pre_category == "Dinner") {
                    carb_check = avgdinner
                }
                if (java.lang.Double.valueOf(total_carbs) > java.lang.Double.valueOf(carb_check) + 0.25 * java.lang.Double.valueOf(
                        carb_check
                    )
                ) {
                    val alertDialog = AlertDialog.Builder(
                        this@PredictionDisplay,R.style.CustomAlertDialog
                    ).create()
                    alertDialog.setCancelable(false)
                    val layoutInflater = LayoutInflater.from(this@PredictionDisplay)
                    val convertView: View =
                        layoutInflater.inflate(R.layout.confirmation_dialogs_layout, null, true)
                    alertDialog.setView(convertView)

                    var yes:CardView=convertView.findViewById(R.id.yes)
                    var no:MaterialCardView=convertView.findViewById(R.id.no)

                    var yesText:TextView=convertView.findViewById(R.id.yes_text)
                    var noText:TextView=convertView.findViewById(R.id.no_text)

                    yesText.setText("Continue")
                    noText.setText("Check Entries")

                    var msg:TextView=convertView.findViewById(R.id.message)
                    msg.setText("Are you sure that you want to proceed to get insulin prediction?")
                    yes.setOnClickListener {
                        alertDialog.cancel()
                    }
                    no.setOnClickListener{
                        var options: ActivityOptions? = null
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            options =
                                ActivityOptions.makeSceneTransitionAnimation(this@PredictionDisplay)
                        }
                        val ii = Intent()
                        ii.setClass(this@PredictionDisplay, MainScreen::class.java)
                        ii.putExtra(DIRECT_INSULIN_SCREEN, "predict")
                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(ii, options!!.toBundle())
                        this@PredictionDisplay.finish()

//                        startActivity(ii,)
                    }
                    alertDialog.show()

                    //////////
//                        val b = AlertDialog.Builder(this@PredictionDisplay)
//                        b.setTitle("Alert")
//                        b.setMessage("Based on the entry done by you the total carb intake in this meal is: ${total_carbs}g which is more than 25% of your usual intake at this meal")
//                        b.setPositiveButton(
//                            "Continue"
//                        ) { dialogInterface, index -> dialogInterface.cancel() }
//                        b.setNegativeButton(
//                            "Check Entries"
//                        ) { dialogInterface, index ->
//                            var options: ActivityOptions? = null
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                options =
//                                    ActivityOptions.makeSceneTransitionAnimation(this@PredictionDisplay)
//                            }
//                            val ii = Intent()
//                            ii.setClass(this@PredictionDisplay, MainScreen::class.java)
//                            ii.putExtra(DIRECT_INSULIN_SCREEN, "predict")
//                            startActivity(ii, options!!.toBundle())
//                            dialogInterface.cancel()
//                        }
//                        b.show()
                }
            }
        }
    }


    private fun CheckExist() {
        try {
            Log.i("see_data", "check")
//            rotateLoading?.visibility=View.VISIBLE
            val preferences = PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
            val key = preferences.getString(KEY, null)

            api = RetrofitHelper.getInstance().create(Prediction::class.java)
            api.getPredictionStatus(key?:"X")?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    var str=response.body()?.string()
                    val jsonObject: JsonObject =
                        JsonParser().parse(str).getAsJsonObject()
                    var xx=jsonObject.get("msg")
//                                    Toast.makeText(this@Login,""+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
                    if((jsonObject.get("code").toString()).equals("1")){
                        api.getPredictionParams(key?:"X")?.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                var params=response.body() as JsonObject

                                val gson = Gson()

                                var trainedParams=gson.fromJson(params,PredictionTrainedParamsFetch::class.java)
                                breakfast_icr = trainedParams.breakfast_icr
//                            Toast.makeText(
//                                this@PredictionDisplay,
//                                "Inside wwww's try block ${breakfast_icr?.toDouble()} ",
//                                Toast.LENGTH_SHORT
//                            ).show()
                                lunch_icr = trainedParams.lunch_icr
                                snack_icr=trainedParams.snack_icr
                                dinner_icr = trainedParams.dinner_icr
                                breakfast_isf = trainedParams.breakfast_isf
                                lunch_isf = trainedParams.lunch_isf
                                snack_isf=trainedParams.snack_isf
                                dinner_isf = trainedParams.dinner_isf
                                myprev_dose = trainedParams.insulin_dose
                                time_prev_dose_in_milli =
                                    trainedParams.prev_insulin_time
                                avgbreakfast = trainedParams.average_breakfast
                                avglunch = trainedParams.average_lunch
                                avgdinner = trainedParams.average_dinner
                                avgsnack=trainedParams.average_snack
                                division_factor = trainedParams.division_by

                                loading.stopLoading()
                                findValues()


                            }
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                loading.stopLoading()
                                Toast.makeText(this@PredictionDisplay,"Could not fetch the details. Please try again.",Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                    else{
//                    Toast.makeText(PredictionDisplay.this, , Toast.LENGTH_SHORT).show();
                        loading.stopLoading()
                        val alertDialog = AlertDialog.Builder(
                            this@PredictionDisplay,R.style.CustomAlertDialog
                        ).create()
                        alertDialog.setCancelable(false)
                        val layoutInflater = LayoutInflater.from(this@PredictionDisplay)
                        val convertView: View =
                            layoutInflater.inflate(R.layout.prediction_not_enabled_dialog, null, true)
                        alertDialog.setView(convertView)

                        var okay:CardView=convertView.findViewById(R.id.okay)
                        okay.setOnClickListener{
                            loading.startLoading()
                            addFoodAndPrediction()

                            val intent = Intent(this@PredictionDisplay, MainScreen::class.java)
                            intent.putExtra(DIRECT_INSULIN_SCREEN , "predict")
//                            intent.putExtra("message", "Food")
//                            intent.putExtra("categorysend", cattemp + "")
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            this@PredictionDisplay.finish()
                            loading.stopLoading()

                            alertDialog.cancel()
                        }
                        alertDialog.show()

//                        val b = AlertDialog.Builder()
//                        b.setMessage("Food entry is recorded. \nMore data is required to start insulin prediction!")
//                        b.setCancelable(false)
//                        b.setPositiveButton(
//                            "OK"
//                        ) { dialogInterface, index ->
//                            dialogInterface.cancel()
//                            AddInFirebaseNoPrediction()
//                        }
//                        b.show()
                    }
//                    for(i in list){
//                        var eachExercise = gson.fromJson(i, FirebaseUserInfo::class.java)
//
//                        x.add(eachExercise.A)
//                        Log.e("lUIRIEURIEIRUE","RJKLJKJKJKLJKLJKLKllllllllllll"+"fdlfdjklfdjkfkjl"+i)
//                        Log.e("lUIRIEURIEIRUE","RJKLJKJKJKLJKLJKLKllllllllllll"+"fdlfdjklfdjkfkjl"+eachExercise.A)
//
//                    }
//                    if(_exercise_name.value==null){
//                        _exercise_name.value=x
//                    }
//                    alertDialog.setView(convertView)
//                    loading.stopLoading()
//                    alertDialog.show()

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    loading.stopLoading()
//                    Log.e("gee",""+t.stackTrace)
                    Toast.makeText(this@PredictionDisplay,"Could not fetch the details. Please try again.",Toast.LENGTH_LONG).show()
//                    loading.stopLoading()
//                    alertDialog.show()
                    Log.e("gee","onFailureeeee"+t.message)
                }
            })

//            myRef2.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    Log.i("see_data", dataSnapshot.getKey().toString())
//                    for (d in dataSnapshot.children) {
//                        Log.i("see_data", d.getKey().toString())
//                        if (d.getKey().toString().equals(key)) {
//                            var data=d.getValue() as HashMap<*,*>
//
//                            val firebasePredictionUser = FirebasePredictionUser(
//                                data["INSULIN_DOSE"].toString() ,
//                                data["BREAKFAST_ISF"].toString(),
//                                data["LUNCH_ISF"].toString(),
//                                data["DINNER_ISF"].toString(),
//                                data["BREAKFAST_ICR"].toString(),
//                                data["LUNCH_ICR"].toString(),
//                                data["DINNER_ICR"].toString(),
//                                data["PREV_INSULIN_TIME"].toString(),
//                                data["AVERAGE_LUNCH"].toString(),
//                                data["AVERAGE_BREAKFAST"].toString(),
//                                data["AVERAGE_DINNER"].toString(),
//                                data["DIVISION_BY"].toString(),
//
//                            )
//
////                            val firebasePredictionUser: FirebasePre1dictionUser = d.getValue(
////                                FirebasePredictionUser::class.java
////                            )!!
//                            breakfast_icr = firebasePredictionUser.bREAKFAST_ICR .toString()
////                            Toast.makeText(
////                                this@PredictionDisplay,
////                                "Inside wwww's try block ${breakfast_icr?.toDouble()} ",
////                                Toast.LENGTH_SHORT
////                            ).show()
//
//                            lunch_icr = firebasePredictionUser.lUNCH_ICR .toString()
//                            dinner_icr = firebasePredictionUser.dINNER_ICR .toString()
//                            breakfast_isf = firebasePredictionUser.bREAKFAST_ISF.toString()
//                            lunch_isf = firebasePredictionUser.lUNCH_ISF.toString()
//                            dinner_isf = firebasePredictionUser.dINNER_ISF.toString()
//                            myprev_dose = firebasePredictionUser.iNSULIN_DOSE.toString()
//                            time_prev_dose_in_milli =
//                                firebasePredictionUser.pREV_INSULIN_TIME .toString()
//                            avgbreakfast = firebasePredictionUser.aVERAGE_BREAKFAST.toString()
//                            avglunch = firebasePredictionUser.aVERAGE_LUNCH.toString()
//                            avgdinner = firebasePredictionUser.aVERAGE_DINNER
//                            division_factor = firebasePredictionUser.dIVISION_BY
//                            went_in = true
//                            break
//                        }
//                    }
//                    if (went_in) {
//                        if (!isNetworkAvailable) {
//                            Toast.makeText(
//                                this@PredictionDisplay,
//                                "No connection",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
////                            Toast.makeText(
////                                this@PredictionDisplay,
////                                "Find values called!!",
////                                Toast.LENGTH_SHORT
////                            ).show()
//
//                            findValues()
//                        }
//                    } else {
//
//
////                    Toast.makeText(PredictionDisplay.this, , Toast.LENGTH_SHORT).show();
//                        val b = AlertDialog.Builder(this@PredictionDisplay)
//                        b.setTitle("Alert")
//                        b.setMessage("Food entry is recorded. \nMore data required to start insulin prediction!")
//                        b.setPositiveButton(
//                            "Ok"
//                        ) { dialogInterface, index ->
//                            dialogInterface.cancel()
//                            AddInFirebaseNoPrediction()
//                        }
//                        b.show()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//            })
        } catch (e: Exception) {
            loading.stopLoading()
            Toast.makeText(this@PredictionDisplay,"Could not fetch the details. Please try again.",Toast.LENGTH_LONG).show()

        }
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    @SuppressLint("Range")
    private fun addFoodWithoutPrediction() {
        try {
            val getpreferences =
                PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
            val storeddate = getpreferences.getString(STORED_DATE, null)
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            val current_date = year.toString() + "-" + (month + 1) + "-" + day
            Log.i("date_issue", "$storeddate in predict $current_date")
            if (storeddate == null || storeddate != current_date) {
                val preferences =
                    PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
                val editor = preferences.edit()
                editor.putString(STORED_DATE, current_date)
                editor.apply()
                editor.commit()
                val count_exercise = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0)
                for (i in 1..count_exercise) {
                    editor.remove(FIXED_WORD_EXERCISE + i)
                    editor.remove(FIXED_EXERCISE_TIME + i)
                }
                val count_insulin = getpreferences.getInt(KEEP_COUNT_INSULIN, 0)
                for (i in 1..count_insulin) {
                    editor.remove(FIXED_INSULIN + i)
                }
                val dataBaseFoodHelperLog = DataBaseFoodHelperLog(this@PredictionDisplay)
                val db = dataBaseFoodHelperLog.writableDatabase
                db.delete(FOOD_HELPER_DB_NAME_LOG, null, null)
                val count_bg = getpreferences.getInt(KEEP_COUNT_BG, 0)
                for (i in 1..count_bg) {
                    editor.remove(FIXED_BG + i)
                }
                editor.putInt(KEEP_COUNT_EXERCISE, 0)
                editor.putInt(KEEP_COUNT_BG, 0)
                editor.putInt(KEEP_COUNT_INSULIN, 0)
                editor.apply()
                editor.commit()
            }
            val dp = dataBaseFoodHelper!!.readableDatabase
            val c = dp.query(FOOD_HELPER_DB_NAME, null, null, null, null, null, null)
            while (c.moveToNext()) {
                var fat: String?
                var protein: String?
                var cal: String?
                food_name = c.getString(c.getColumnIndex(FOOD_DB_NAME))
                food_carbs = c.getString(c.getColumnIndex(CARBS))
                quantity = c.getString(c.getColumnIndex(FOOD_DB_QUANTITY))
                id = c.getInt(c.getColumnIndex(FOOD_HELPER_DB_ID))
                time_stamp = c.getLong(c.getColumnIndex(FOOD_DB_TIME_STAMP))
                fat = c.getString(c.getColumnIndex(FAT))
                protein = c.getString(c.getColumnIndex(PROTEIN))
                cal = c.getString(c.getColumnIndex(CALORIES))
                val preferences =
                    PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
                val key = preferences.getString(KEY, null)
                val current_time = Calendar.getInstance()
                val hour = current_time[Calendar.HOUR_OF_DAY]
                val minute = current_time[Calendar.MINUTE]
                val curr__time = "$hour:$minute"
                //            Calendar mcurrenttime = Calendar.getInstance();
//            int year = mcurrenttime.get(Calendar.YEAR);
//            int month = mcurrenttime.get(Calendar.MONTH);
//            int day = mcurrenttime.get(Calendar.DAY_OF_MONTH);
//            String current_date = day + "/" + (month + 1) + "/" + year;


                val formatter = SimpleDateFormat("yyyy-MM-dd")
                var finalDateN = formatter.parse(prev_date?.toString())

                var food= FoodEntry(key?:"NULL",finalDateN,prev_time.toString(),food_name.toString(),pre_category.toString(),quantity.toString().toDouble())
                apiFood.addFood(food)?.enqueue(object :
                    Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        var str=response.body()?.string()
                        val jsonObject: JsonObject =
                            JsonParser().parse(str).getAsJsonObject()
                        var xx=jsonObject.get("msg")
                        Toast.makeText(this@PredictionDisplay,""+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
                        if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){
//                                        loading.stopLoading()
                        }
                        else{
                            //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        requireContext().finish()

                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("gee",""+t.stackTrace)
//                    Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()

                        Log.e("gee","onFailureeeee"+t.message)

                        // handle the failure
                    }
                })



//                foodref.child(key!!).child(time_stamp.toString() + "").child("DATE")
//                    .setValue(prev_date)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("FOOD_CATEGORY")
//                    .setValue(pre_category)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("FOOD_NAME")
//                    .setValue(food_name)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("QUANTITY")
//                    .setValue(quantity)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("TIME")
//                    .setValue(prev_time)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("CURRENT_DATE")
//                    .setValue(current_date)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("CURRENT_TIME")
//                    .setValue(curr__time)
                val dataBaseFoodHelperLog = DataBaseFoodHelperLog(this@PredictionDisplay)
                val db = dataBaseFoodHelperLog.writableDatabase
                val cv = ContentValues()
                cv.put(FOOD_DB_NAME, food_name)
                cv.put(FOOD_DB_TIME_STAMP, time_stamp.toString() + "")
                cv.put(FOOD_DB_QUANTITY, quantity)
                cv.put(USER_DATE_ENTERED, prev_date)
                cv.put(USER_TIME_ENTERED, prev_time)
                cv.put(DB_CATEGORY_ENTERED, pre_category)
                cv.put(FAT, fat)
                cv.put(CARBS, food_carbs)
                cv.put(PROTEIN, protein)
                cv.put(CALORIES, cal)
                //
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            String key = preferences.getString(KEY, null);
//
//            Calendar current_time = Calendar.getInstance();
//            int hour = current_time.get(Calendar.HOUR_OF_DAY);
//            int minute = current_time.get(Calendar.MINUTE);
//            String curr__time = hour + ":" + minute;
//            long time_as_timestamp = System.currentTimeMillis();
////            Log.i("final_testing_firebase", key+" | "+time_as_timestamp+" | "+food_name + " | " + time_stamp + " | " + quantity + " | " + date_text.getText().toString() + " | " + time_text.getText().toString() + " | " + category_.getText().toString());
//
//            foodref.child(key).child(time_as_timestamp + "").child("DATE").setValue(date_text.getText().toString());
//            foodref.child(key).child(time_as_timestamp + "").child("FOOD_CATEGORY").setValue(category_.getText().toString());
//            foodref.child(key).child(time_as_timestamp + "").child("FOOD_NAME").setValue(food_name);
//            foodref.child(key).child(time_as_timestamp + "").child("QUANTITY").setValue(quantity);
//            foodref.child(key).child(time_as_timestamp + "").child("TIME").setValue(time_text.getText().toString());
//            foodref.child(key).child(time_as_timestamp + "").child("CURRENT_DATE").setValue(current_date);
//            foodref.child(key).child(time_as_timestamp + "").child("CURRENT_TIME").setValue(curr__time);
//
//
                db.insert(FOOD_HELPER_DB_NAME_LOG, null, cv)
                //
//
            }
            val dbread = dataBaseFoodHelper!!.readableDatabase
            dbread.delete(FOOD_HELPER_DB_NAME, null, null)
            val preferences_contents =
                PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
            val editor = preferences_contents.edit()
            editor.remove(PREDICT_SCREEN_CATEGORY)
            editor.remove(CURRENT_BG)
            editor.remove(TIME_IN_MIN_INSULIN)
            editor.remove(TOTAL_CARBS_FOR_PREDICTION)
            editor.remove(CURRENT_DATE)
            editor.remove(CURRENT_TIME)
            editor.apply()
            editor.commit()
//            rotateLoading?.visibility=View.INVISIBLE
            val i = Intent()
            i.setClass(this@PredictionDisplay, MainScreen::class.java)
            startActivity(i)
        } catch (e: Exception) {
        }
    }

    @SuppressLint("Range")
    private fun addFoodAndPrediction() {
        try {
            val getpreferences =
                PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
            val storeddate = getpreferences.getString(STORED_DATE, null)
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            val current_date = year.toString() + "-" + (month + 1) + "-" + day
            Log.i("date_issue", "$storeddate in predict $current_date")
            if (storeddate == null || storeddate != current_date) {
                val preferences =
                    PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
                val editor = preferences.edit()
                editor.putString(STORED_DATE, current_date)
                editor.apply()
                editor.commit()
                val count_exercise = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0)
                for (i in 1..count_exercise) {
                    editor.remove(FIXED_WORD_EXERCISE + i)
                    editor.remove(FIXED_EXERCISE_TIME + i)
                }
                val count_insulin = getpreferences.getInt(KEEP_COUNT_INSULIN, 0)
                for (i in 1..count_insulin) {
                    editor.remove(FIXED_INSULIN + i)
                }
                val dataBaseFoodHelperLog = DataBaseFoodHelperLog(this@PredictionDisplay)
                val db = dataBaseFoodHelperLog.writableDatabase
                db.delete(FOOD_HELPER_DB_NAME_LOG, null, null)
                val count_bg = getpreferences.getInt(KEEP_COUNT_BG, 0)
                for (i in 1..count_bg) {
                    editor.remove(FIXED_BG + i)
                }
                editor.putInt(KEEP_COUNT_EXERCISE, 0)
                editor.putInt(KEEP_COUNT_BG, 0)
                editor.putInt(KEEP_COUNT_INSULIN, 0)
                editor.apply()
                editor.commit()
            }
            val dp = dataBaseFoodHelper!!.readableDatabase
            val c = dp.query(FOOD_HELPER_DB_NAME, null, null, null, null, null, null)
            while (c.moveToNext()) {
                var fat: String?
                var protein: String?
                var cal: String?
                food_name = c.getString(c.getColumnIndex(FOOD_DB_NAME))
                food_carbs = c.getString(c.getColumnIndex(CARBS))
                quantity = c.getString(c.getColumnIndex(FOOD_DB_QUANTITY))
                id = c.getInt(c.getColumnIndex(FOOD_HELPER_DB_ID))
                time_stamp = c.getLong(c.getColumnIndex(FOOD_DB_TIME_STAMP))
                fat = c.getString(c.getColumnIndex(FAT))
                protein = c.getString(c.getColumnIndex(PROTEIN))
                cal = c.getString(c.getColumnIndex(CALORIES))
                val preferences =
                    PreferenceManager.getDefaultSharedPreferences(this@PredictionDisplay)
                val key = preferences.getString(KEY, null)
                val current_time = Calendar.getInstance()
                val hour = current_time[Calendar.HOUR_OF_DAY]
                val minute = current_time[Calendar.MINUTE]
                val curr__time = "$hour:$minute"
                //            Calendar mcurrenttime = Calendar.getInstance();
//            int year = mcurrenttime.get(Calendar.YEAR);
//            int month = mcurrenttime.get(Calendar.MONTH);
//            int day = mcurrenttime.get(Calendar.DAY_OF_MONTH);
//            String current_date = day + "/" + (month + 1) + "/" + year;

                val formatter = SimpleDateFormat("yyyy-MM-dd")
                var finalDateN = formatter.parse(prev_date?.toString())

                var food= FoodEntry(key?:"NULL",finalDateN,prev_time.toString(),food_name.toString(),pre_category.toString(),quantity.toString().toDouble())
                apiFood.addFood(food)?.enqueue(object :
                    Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        var str=response.body()?.string()
                        val jsonObject: JsonObject =
                            JsonParser().parse(str).getAsJsonObject()
                        var xx=jsonObject.get("msg")
                        Toast.makeText(this@PredictionDisplay,""+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
                        if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){
//                                        loading.stopLoading()
                        }
                        else{
                            //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        requireContext().finish()

                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("gee",""+t.stackTrace)
//                    Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()

                        Log.e("gee","onFailureeeee"+t.message)

                        // handle the failure
                    }
                })

                var pred= PredictionExtraDetails(key?:"NULL",curr_bg.toString().toDouble(),finalDateN,prev_time.toString(),pre_category.toString(),quantity.toString().toDouble(),food_name.toString())
                apiPred.addExtraDetails(pred)?.enqueue(object :
                    Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        var str=response.body()?.string()
                        val jsonObject: JsonObject =
                            JsonParser().parse(str).getAsJsonObject()
                        var xx=jsonObject.get("msg")
                        Toast.makeText(this@PredictionDisplay,""+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
                        if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){
//                                        loading.stopLoading()
                        }
                        else{
                            //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        requireContext().finish()

                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("gee",""+t.stackTrace)
//                    Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()

                        Log.e("gee","onFailureeeee"+t.message)

                        // handle the failure
                    }
                })



//                foodref.child(key!!).child(time_stamp.toString() + "").child("DATE")
//                    .setValue(prev_date)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("FOOD_CATEGORY")
//                    .setValue(pre_category)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("FOOD_NAME")
//                    .setValue(food_name)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("QUANTITY")
//                    .setValue(quantity)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("TIME")
//                    .setValue(prev_time)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("CURRENT_DATE")
//                    .setValue(current_date)
//                foodref.child(key!!).child(time_stamp.toString() + "").child("CURRENT_TIME")
//                    .setValue(curr__time)
//                preref.child(key!!).child(time_stamp.toString() + "").child("DATE")
//                    .setValue(prev_date)
//                preref.child(key!!).child(time_stamp.toString() + "").child("FOOD_CATEGORY")
//                    .setValue(pre_category)
//                preref.child(key!!).child(time_stamp.toString() + "").child("FOOD_NAME")
//                    .setValue(food_name)
//                preref.child(key!!).child(time_stamp.toString() + "").child("QUANTITY")
//                    .setValue(quantity)
//                preref.child(key!!).child(time_stamp.toString() + "").child("TIME")
//                    .setValue(prev_time)
//                preref.child(key!!).child(time_stamp.toString() + "").child("CURRENT_DATE")
//                    .setValue(current_date)
//                preref.child(key!!).child(time_stamp.toString() + "").child("CURRENT_TIME")
//                    .setValue(curr__time)
//                preref.child(key!!).child(time_stamp.toString() + "").child("CURRENT_BG")
//                    .setValue(curr_bg)
                val dataBaseFoodHelperLog = DataBaseFoodHelperLog(this@PredictionDisplay)
                val db = dataBaseFoodHelperLog.writableDatabase
                val cv = ContentValues()
                cv.put(FOOD_DB_NAME, food_name)
                cv.put(FOOD_DB_TIME_STAMP, time_stamp.toString() + "")
                cv.put(FOOD_DB_QUANTITY, quantity)
                cv.put(USER_DATE_ENTERED, prev_date)
                cv.put(USER_TIME_ENTERED, prev_time)
                cv.put(DB_CATEGORY_ENTERED, pre_category)
                cv.put(FAT, fat)
                cv.put(CARBS, food_carbs)
                cv.put(PROTEIN, protein)
                cv.put(CALORIES, cal)
                //
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            String key = preferences.getString(KEY, null);
//
//            Calendar current_time = Calendar.getInstance();
//            int hour = current_time.get(Calendar.HOUR_OF_DAY);
//            int minute = current_time.get(Calendar.MINUTE);
//            String curr__time = hour + ":" + minute;
//            long time_as_timestamp = System.currentTimeMillis();
////            Log.i("final_testing_firebase", key+" | "+time_as_timestamp+" | "+food_name + " | " + time_stamp + " | " + quantity + " | " + date_text.getText().toString() + " | " + time_text.getText().toString() + " | " + category_.getText().toString());
//
//            foodref.child(key).child(time_as_timestamp + "").child("DATE").setValue(date_text.getText().toString());
//            foodref.child(key).child(time_as_timestamp + "").child("FOOD_CATEGORY").setValue(category_.getText().toString());
//            foodref.child(key).child(time_as_timestamp + "").child("FOOD_NAME").setValue(food_name);
//            foodref.child(key).child(time_as_timestamp + "").child("QUANTITY").setValue(quantity);
//            foodref.child(key).child(time_as_timestamp + "").child("TIME").setValue(time_text.getText().toString());
//            foodref.child(key).child(time_as_timestamp + "").child("CURRENT_DATE").setValue(current_date);
//            foodref.child(key).child(time_as_timestamp + "").child("CURRENT_TIME").setValue(curr__time);
//
//
                db.insert(FOOD_HELPER_DB_NAME_LOG, null, cv)
                //
//
            }
            val dbread = dataBaseFoodHelper!!.readableDatabase
            dbread.delete(FOOD_HELPER_DB_NAME, null, null)
        } catch (e: Exception) {
        }
    }
}





