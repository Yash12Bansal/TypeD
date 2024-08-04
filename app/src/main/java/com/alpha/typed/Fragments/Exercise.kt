package com.alpha.typed.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
//import com.google.firebase.database.*
import com.alpha.typed.Activities.DataBaseFoodHelperLog
import java.util.*
import com.alpha.typed.Classes.Constants.CARBS
import com.alpha.typed.Classes.Constants.FAT
import com.alpha.typed.Classes.Constants.FIXED_BG
import com.alpha.typed.Classes.Constants.FIXED_EXERCISE_TIME
import com.alpha.typed.Classes.Constants.FIXED_INSULIN
import com.alpha.typed.Classes.Constants.FIXED_WORD_EXERCISE
import com.alpha.typed.Classes.Constants.FOOD_DB_QUANTITY
import com.alpha.typed.Classes.Constants.FOOD_HELPER_DB_NAME_LOG
import com.alpha.typed.Classes.Constants.KEEP_COUNT_BG
import com.alpha.typed.Classes.Constants.KEEP_COUNT_EXERCISE
import com.alpha.typed.Classes.Constants.KEEP_COUNT_INSULIN
import com.alpha.typed.Classes.Constants.KEY
import com.alpha.typed.Classes.Constants.PROTEIN
import com.alpha.typed.Classes.Constants.STORED_DATE
import com.alpha.typed.CustomDialog.AddNewExerciseModalSheet
import com.alpha.typed.R
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityMainScreenBinding
import com.alpha.typed.viewModels.ExerciseViewModel
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Exercise.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Exercise.newInstance] factory method to
 * create an instance of this fragment.
 */
class Exercise() : Fragment() {
    var time1: Long = 0
    var time2: Long = 0
    lateinit var exercise: String
    lateinit var current_date: String
    lateinit var current__time: String
    var exercise_id = 0
    lateinit var exercise_text: TextView
    lateinit var dataBaseFoodHelperlog: DataBaseFoodHelperLog
    var current_time = Calendar.getInstance()
    var final_date = ""
    var final_time_start = ""
    var final_time_end = ""
    var exercise_name = mutableListOf<String>()
    lateinit var date: CardView
    lateinit var start_time: CardView
    lateinit var end_time: CardView
    lateinit var date_text: TextView
    lateinit var start_time_text: TextView
    lateinit var end_time_text: TextView
    lateinit var cardView: CardView
    lateinit var add_exercise: CardView
    lateinit var lv: ListView
    lateinit var present_date_compare: String
    lateinit var submit: MaterialCardView
    lateinit var date_cross: ImageButton
    lateinit var start_time_cross: ImageButton
    lateinit var end_time_cross: ImageButton
    var carbs = 0f
    var fat = 0f
    var proteins = 0f
    var quantity = 0f
    var dataBaseFoodHelper: DataBaseFoodHelperLog? = null
    var pieChart: PieChart? = null
    var exercise_list = mutableMapOf<String,Float>()
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    lateinit var loading: Loading<ActivityMainScreenBinding>
    lateinit var rotateloading:View
    lateinit var screen:View
    lateinit var myViewModel :ExerciseViewModel
    private var mListener: OnFragmentInteractionListener?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_exercise, container, false)
        //        spinner = (Spinner) view.findViewById(R.id.Spinner);
        if (!isNetworkAvailable) {
            Toast.makeText(activity, "No connection", Toast.LENGTH_SHORT).show()
        }
        myViewModel= ViewModelProvider(this).get(ExerciseViewModel::class.java)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        CheckExist()
        pieChart = view.findViewById<View>(R.id.piechart) as PieChart
        rotateloading=view.findViewById(R.id.rotateloading)
        rotateloading.visibility=View.INVISIBLE
        screen = view.findViewById(R.id.screen)
        loading= Loading<ActivityMainScreenBinding>(requireActivity(),rotateloading,screen)

        pieChart!!.setUsePercentValues(true)
        dataBaseFoodHelperlog = DataBaseFoodHelperLog(activity)
        date = view.findViewById(R.id.date_button)
        start_time = view.findViewById(R.id.start_time_button)
        end_time = view.findViewById(R.id.end_time_button)
        date_text = view.findViewById<View>(R.id.date_text) as TextView
        start_time_text = view.findViewById<View>(R.id.start_time_text) as TextView
        end_time_text = view.findViewById<View>(R.id.end_time_text) as TextView
        submit = view.findViewById(R.id.submit_button)
        add_exercise = view.findViewById(R.id.add_exercise)
        cardView = view.findViewById(R.id.exercise_name)
        exercise_text = view.findViewById(R.id.exercise_textview)
        date_cross = view.findViewById(R.id.date_cross)
        start_time_cross = view.findViewById(R.id.start_time_cross)
        end_time_cross = view.findViewById(R.id.end_time_cross)
        exercise_context = activity
        val getpreferences = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        val count_exercise = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0)
        for (i in 1..count_exercise) {
            val exercise_item = getpreferences.getString(FIXED_WORD_EXERCISE + i, null)
            val duration = getpreferences.getFloat(FIXED_EXERCISE_TIME + i, 0f)
            exercise_list.put(exercise_item.toString(),duration)
//            exercise_list.add("1) EXERCISE - $exercise_item\n    DURATION - $duration min")
        }
//        full_carbs?.setText((Math.round(carbs * 100.0) / 100.0).toString() + "" + " g")
        dataBaseFoodHelper = DataBaseFoodHelperLog(activity)
        setUpViews()
        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()
        if (exercise_list.isEmpty()) {
        } else {
            for(i in exercise_list.keys){
                labels.add(i)
            }
//        labels.add("carbs")
//        labels.add("fats")
//        labels.add("proteins")
            var index=-1
            var centerText=0f
            if(exercise_list!=null){
            }
            for(i in exercise_list.keys){
                index++
                entries.add(Entry(exercise_list[i]!!,index))
                centerText+=exercise_list[i]!!
            }
            val dataSet = PieDataSet(entries, "")
            val data = PieData(labels, dataSet)
            val arr = arrayOf(
                ContextCompat.getColor(requireActivity(), R.color.c1),
                ContextCompat.getColor(requireActivity(),R.color.c2)
            )
//                ContextCompat.getColor(requireActivity(),R.color.yellow),
//                ContextCompat.getColor(requireActivity(),R.color.orange),
//                ContextCompat.getColor(requireActivity(),R.color.green),
//                ContextCompat.getColor(requireActivity(),R.color.pink),
//                ContextCompat.getColor(requireActivity(),R.color.gy),
//                ContextCompat.getColor(requireActivity(),R.color.lb)
//                .createColors(arr.toIntArray())
            dataSet.setColors(ColorTemplate.LIBERTY_COLORS)
            data.setValueTextSize(14f)
            data.setValueFormatter(PercentFormatter())
            pieChart!!.setDescription("")
            pieChart!!.animateXY(1000, 1000)
            pieChart!!.animate()
            pieChart!!.setData(data)
            pieChart!!.centerText="${centerText.toInt()} mins"
            pieChart!!.setCenterTextSize(25f)
            pieChart!!.setCenterTextColor(Color.BLACK)
            pieChart!!.setHoleRadius(90f)
            pieChart!!.invalidate()

        }
//        entries.add(Entry (carbs, 0))
//        entries.add(Entry(fat, 1))
//        entries.add(Entry(proteins, 2))
        val mcurrenttime = Calendar.getInstance()
        val year = mcurrenttime[Calendar.YEAR]
        val month = mcurrenttime[Calendar.MONTH]
        val day = mcurrenttime[Calendar.DAY_OF_MONTH]
        present_date_compare = day.toString() + "/" + (month + 1) + "/" + year
        val default_date = day.toString() + "/" + (month + 1) + "/" + year
        val hour = mcurrenttime[Calendar.HOUR_OF_DAY]
        val minute = mcurrenttime[Calendar.MINUTE]
        val default_time = "$hour:$minute"
        date_text.text = default_date
        date_cross.setOnClickListener(View.OnClickListener {
            date_text.text = "Date"
            final_date = ""
        })
        start_time_cross.setOnClickListener(View.OnClickListener {
            start_time_text.text = "Start-Time"
            final_time_start = ""
        })
        end_time_cross.setOnClickListener(View.OnClickListener {
            end_time_text.text = "End-Time"
            final_time_end = ""
        })

//                if (!exercise_name.isEmpty())dff
        val alertDialog = AlertDialog.Builder(
            activity
        ).create()
        alertDialog.setCancelable(true)
        val layoutInflater = LayoutInflater.from(activity)
        val convertView: View =
            layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
//        alertDialog.setTitle("Exercise List")

        lv = convertView.findViewById<View>(R.id.lv) as ListView
        lv.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val selectedFromList = lv.getItemAtPosition(i).toString()
            exercise_id = i
            exercise = selectedFromList
            exercise_text.setText(exercise)
            alertDialog.cancel()
        }
        val adapter = ArrayAdapter(
            requireContext(), R.layout.search_food_db_list_item, exercise_name)
        lv.adapter = adapter
        myViewModel.exercise_name.observe(viewLifecycleOwner,{
            Log.e("ts","RRRRRRREEQWTTTTTTTTTTTTTTTTTTTTTTTT"+it)
            exercise_name.addAll(it)

            adapter.addAll(it)

//            if(it.size!=0 && it!=null){

            adapter.notifyDataSetChanged()
//            alertDialog.setView(convertView)
//            alertDialog.show()

//            }
        })

        cardView.setOnClickListener(View.OnClickListener {
            myViewModel.getExercises(alertDialog,loading,convertView)
            loading.startLoading()
//            alertDialog.show()

        //            api.getExercises()?.enqueue(object : Callback<List<JsonObject>> {
//                override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
//                    var list=response.body() as List<JsonObject>
//
//                    val gson = Gson()
//
//                    for(i in list){
//                        var eachExercise = gson.fromJson(i, FirebaseUserInfo::class.java)
//
//                        exercise_name.add(eachExercise.A)
//                        Log.e("lUIRIEURIEIRUE","RJKLJKJKJKLJKLJKLKllllllllllll"+"fdlfdjklfdjkfkjl"+i)
//                        Log.e("lUIRIEURIEIRUE","RJKLJKJKJKLJKLJKLKllllllllllll"+"fdlfdjklfdjkfkjl"+eachExercise.A)
//
//                    }

////////////////////888888888888888888888888888888888888888888888888888
//                    val jsonObject: JsonObject =
//                        JsonParser().parse(str).getAsJsonObject()
//                    var xx=jsonObject.get("msg")
//                    Log.e("thisisis",str!!)
//                    Toast.makeText(activity,"fdfdfdfee"+str ,Toast.LENGTH_LONG).show()
//                    if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){
//                        Toast.makeText(requireContext(),"fdfdfdfee"+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
//
//                        loading.stopLoading()
//                        startActivity(
//                            Intent(requireContext(), MainScreen::class.java).addFlags(
//                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                        this@Details10.activity?.finish()
//                    }
//                    else{
//                        Toast.makeText(requireContext(),"fdfdfdfee"+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
//                        loading.stopLoading()
//                        startActivity(
//                            Intent(requireContext(), MainScreen::class.java).addFlags(
//                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                        this@Details10.activity?.finish()
//
                        //                            loading.stopLoading()
//                            startActivity(
//                                Intent(this@Login, EnterDetails::class.java).addFlags(
//                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                            this@Login.finish()

                        //                                        startActivity(Intent(this@Login, MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        this@Login.finish()

//                    }
//                }

//                override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
//                    Log.e("gee",""+t.stackTrace)
////                    Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()
//
//                    Log.e("gee","onFailureeeee"+t.message)
//
//                    // handle the failure
//                }
//            })

//            val alertDialog = AlertDialog.Builder(
//                activity
//            ).create()
//            alertDialog.setCancelable(true)
//            if (!isNetworkAvailable) {
//                Toast.makeText(activity, "No connection", Toast.LENGTH_SHORT).show()
//            } else {
//                if (exercise_name.isEmpty()) {
//                    Toast.makeText(activity, "Empty List", Toast.LENGTH_SHORT).show()
//
//                    CheckExist()
//                }
//                val layoutInflater = LayoutInflater.from(activity)
//                val convertView: View =
//                    layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
//                alertDialog.setTitle("Exercise List")
//                lv = convertView.findViewById<View>(R.id.lv) as ListView
//                val adapter = ArrayAdapter(
//                    activity, android.R.layout.simple_list_item_1, exercise_name
//                )
//                lv.adapter = adapter
//                lv.onItemClickListener =
//                    AdapterView.OnItemClickListener { adapterView, view, i, l ->
//                        val selectedFromList = lv.getItemAtPosition(i).toString()
//                        //                            Log.i("Check2", selectedFromList + "  " + i);
//                        exercise_id = i
//                        exercise = selectedFromList
//                        exercise_text.setText(exercise)
//                        alertDialog.cancel()
//                    }
//                alertDialog.setView(convertView)
////                if (!exercise_name.isEmpty())
//                alertDialog.show()
////                else {
////                    Toast.makeText(activity, "Check Connection", Toast.LENGTH_SHORT).show()
////                }
//            }
        })
        add_exercise.setOnClickListener(View.OnClickListener {
            val bottomSheet = AddNewExerciseModalSheet(loading)
            activity?.supportFragmentManager.let { bottomSheet.show(it!!, "TAG") }//            val cdd = ExerciseDialog(this.requireActivity())
//            cdd.show()
        })
        date.setOnClickListener(View.OnClickListener {
            val mcurrenttime = Calendar.getInstance()
            val year = mcurrenttime[Calendar.YEAR]
            val month = mcurrenttime[Calendar.MONTH]
            val day = mcurrenttime[Calendar.DAY_OF_MONTH]
            current_date = year.toString() + "-" + (month + 1) + "-" + day
            present_date_compare = year.toString() + "-" + (month + 1) + "-" + day
            val dialog = DatePickerDialog(this.requireContext(), android.R.style.Theme_Holo_Light_Dialog,
                { datePicker, year, month, day ->
                    final_date = year.toString() + "-" + (month + 1) + "-" + day
                    date_text.text = final_date
                }, year, month, day
            )
            val minDay = day-7
            val minMonth = month
            val minYear = year
            mcurrenttime.set(minYear, minMonth, minDay)
            dialog.datePicker.minDate = mcurrenttime.timeInMillis

            val maxDay = day
            val maxMonth = month
            val maxYear = year
            mcurrenttime.set(maxYear, maxMonth, maxDay)
            dialog.datePicker.maxDate = mcurrenttime.timeInMillis

            // Display the calendar dialog
            dialog.show()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        })
        start_time.setOnClickListener(object : View.OnClickListener {
            var hour = current_time[Calendar.HOUR_OF_DAY]
            var minute = current_time[Calendar.MINUTE]
            override fun onClick(view: View) {
                current__time = "$hour:$minute"
                val timePickerDialog = TimePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog,
                    { timePicker, i, i1 ->
                        start_time_text!!.text = "$i:$i1"
                        final_time_start = "$i:$i1"
                        time1 = (i * 60 + i1).toLong()
                    }, hour, minute, true
                )
                timePickerDialog.show()
            }
        })
        end_time.setOnClickListener(object : View.OnClickListener {
            var hour = current_time[Calendar.HOUR_OF_DAY]
            var minute = current_time[Calendar.MINUTE]
            override fun onClick(view: View) {
//                current_end_time=hour+":"+minute;
                val timePickerDialog = TimePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog,
                    { timePicker, i, i1 ->
                        end_time_text!!.text = "$i:$i1"
                        final_time_end = "$i:$i1"
                        time2 = (i * 60 + i1).toLong()
                    }, hour, minute, true
                )
                timePickerDialog.show()
            }
        })
        submit.setOnClickListener(object : View.OnClickListener {
            var test = "keyhere"
            override fun onClick(view: View) {
                try {
//                    val b = AlertDialog.Builder(
//                        activity
//                    )
//                    b.setTitle("Alert")
//                    b.setMessage("Are you sure you want to submit ?")
//                    b.setPositiveButton(
//                        "Yes"
//                    ) { dialogInterface, index ->
                        if (!isNetworkAvailable) {
                            Toast.makeText(activity, "No connection", Toast.LENGTH_SHORT).show()
                        } else if ("Exercise Name" == exercise_text!!.getText()
                                .toString() || "" == exercise_text!!.getText().toString()
                        ) {
                            Toast.makeText(activity, "Enter exercise name", Toast.LENGTH_SHORT)
                                .show()
                        } else if ("" == date_text!!.text.toString()) {
                            Toast.makeText(activity, "Enter Date", Toast.LENGTH_SHORT).show()
                        } else if ("" == final_time_start) {
                            Toast.makeText(activity, "Enter start time", Toast.LENGTH_SHORT).show()
                        } else if ("" == final_time_end) {
                            Toast.makeText(activity, "Enter end time", Toast.LENGTH_SHORT).show()
                        } else if (time2 <= time1) {
                            Toast.makeText(activity, "Invalid time entries", Toast.LENGTH_SHORT)
                                .show()
                        } else if (exercise_text!!.getText().toString()
                                .lowercase(Locale.getDefault()).contains("none")
                        ) {
                            Toast.makeText(
                                activity,
                                "Add the new exercise in database",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
                            val key = preferences.getString(KEY, null)
                            val time = System.currentTimeMillis()
                            val formatter = SimpleDateFormat("yyyy-MM-dd")
                            var finalDateN = formatter.parse(final_date)
                            loading.startLoading()

                            myViewModel.postExercise(loading,key?:"NULL",current__time,final_time_end,final_time_start,finalDateN,exercise)
//                            api.postExercise(exerciseEntry)?.enqueue(object : Callback<ResponseBody> {
//                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                                    var str=response.body()?.string()
//                                    val jsonObject: JsonObject =
//                                        JsonParser().parse(str).getAsJsonObject()
//                                    var xx=jsonObject.get("msg")
//                                    Toast.makeText(activity!!,""+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
//                                    if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){
////                                        loading.stopLoading()
//                                    }
//                                    else{
//                                        //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
////                                        requireContext().finish()
//
//                                    }
//
//                                    }
//
////                                    val alertDialog = AlertDialog.Builder(
////                                        activity
////                                    ).create()
////                                    alertDialog.setCancelable(true)
////                                    val layoutInflater = LayoutInflater.from(activity)
////                                    val convertView: View =
////                                        layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
////                                    alertDialog.setTitle("Exercise List")
////                                    lv = convertView.findViewById<View>(R.id.lv) as ListView
////                                    val adapter = ArrayAdapter(
////                                        activity, android.R.layout.simple_list_item_1, exercise_name
////                                    )
////                                    lv.adapter = adapter
////                                    lv.onItemClickListener =
////                                        AdapterView.OnItemClickListener { adapterView, view, i, l ->
////                                            val selectedFromList = lv.getItemAtPosition(i).toString()
////                                            //                            Log.i("Check2", selectedFromList + "  " + i);
////                                            exercise_id = i
////                                            exercise = selectedFromList
////                                            exercise_text.setText(exercise)
////                                            alertDialog.cancel()
////                                        }
////                                    alertDialog.setView(convertView)
//////                if (!exercise_name.isEmpty())
////                                    alertDialog.show()
//
//
//
//                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                                    Log.e("gee",""+t.stackTrace)
////                    Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()
//
//                                    Log.e("gee","onFailureeeee"+t.message)
//
//                                    // handle the failure
//                                }
//                            })
//


                            //                            Log.i("Check5", key + " hello");
//                            myRef.child(key!!).child(time.toString() + "").child("EXERCISE")
//                                .setValue(exercise_id)
//                            myRef.child(key).child(time.toString() + "").child("DATE").setValue(
//                                date_text!!.text.toString()
//                            )
//                            myRef.child(key).child(time.toString() + "").child("END_TIME")
//                                .setValue(final_time_end)
//                            myRef.child(key).child(time.toString() + "").child("START_TIME")
//                                .setValue(final_time_start)
//                            myRef.child(key).child(time.toString() + "").child("CURRENT_DATE")
//                                .setValue(current_date)
//                            myRef.child(key).child(time.toString() + "").child("CURRENT_TIME")
//                                .setValue(current__time)
                            exercise_text!!.setText("Exercise Name")
                            date_text!!.text = "Date"
                            start_time_text!!.text = "Start-Time"
                            end_time_text!!.text = "End-Time"
                            val getpreferences = PreferenceManager.getDefaultSharedPreferences(
                                activity
                            )
                            var count_here = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0)
                            val preferences_contents =
                                PreferenceManager.getDefaultSharedPreferences(
                                    activity
                                )
                            val editor = preferences_contents.edit()
                            var storeddate = getpreferences.getString(STORED_DATE, null)
                            val carb_count = 0.0f
                            val fat_count = 0.0f
                            val protein_count = 0.0f
                            if (storeddate == null || storeddate != present_date_compare) {
                                //                                Log.i("checking21", storeddate + " " + present_date_compare);
                                //                                    carb_count = 0.0f;
                                //                                    fat_count = 0.0f;
                                //                                    protein_count = 0.0f;

                                //                                    editor.putFloat(FAT, fat_count);
                                //                                    editor.putFloat(CARBS, carb_count);
                                //                                    editor.putFloat(PROTEIN, protein_count);
                                storeddate = present_date_compare
                                editor.putString(STORED_DATE, storeddate)
                                //                                    int count = getpreferences.getInt(KEEP_COUNT, 0);
                                //                                    for (int i = 1; i <= count; i++) {
                                //                                        editor.remove(FIXED_WORD + i);
                                //                                        editor.remove(FIXED_AMOUNT + i);
                                //                                        editor.remove(FOOD_CATEGORY + i);
                                //                                        editor.remove(FIXED_CARBS + i);
                                //                                        editor.remove(CALORIES + i);
                                //                                    }
                                val dbread = dataBaseFoodHelperlog!!.readableDatabase
                                dbread.delete(FOOD_HELPER_DB_NAME_LOG, null, null)
                                count_here = 0
                                val count_exercise = getpreferences.getInt(KEEP_COUNT_EXERCISE, 0)
                                for (i in 1..count_exercise) {
                                    editor.remove(FIXED_WORD_EXERCISE + i)
                                    editor.remove(FIXED_EXERCISE_TIME + i)
                                }
                                val count_insulin = getpreferences.getInt(KEEP_COUNT_INSULIN, 0)
                                for (i in 1..count_insulin) {
                                    editor.remove(FIXED_INSULIN + i)
                                }
                                val count_bg = getpreferences.getInt(KEEP_COUNT_BG, 0)
                                for (i in 1..count_bg) {
                                    editor.remove(FIXED_BG + i)
                                }
                                editor.putInt(KEEP_COUNT_EXERCISE, 0)
                                editor.putInt(KEEP_COUNT_BG, 0)
                                editor.putInt(KEEP_COUNT_INSULIN, 0)
                            }
                            count_here++
                            editor.putInt(KEEP_COUNT_EXERCISE, count_here)
                            editor.putString(
                                FIXED_WORD_EXERCISE + count_here,
                                exercise_name[exercise_id]
                            )
                            val duration = java.lang.Float.valueOf((time2 - time1 + 0.0).toString())
                            editor.putFloat(FIXED_EXERCISE_TIME + count_here, duration)
                            editor.apply()
                            editor.commit()

                            //                            Log.i("checking21", duration + " " + exercise_name.get(exercise_id));
                            final_date = ""
                            final_time_start = ""
                            final_time_end = ""
                            Toast.makeText(activity, "Data submitted successfully!", Toast.LENGTH_SHORT)
                                .show()
                        }
//                        dialogInterface.cancel()
//                    }
//                    b.setNegativeButton(
//                        "No"
//                    ) { dialogInterface, i -> dialogInterface.cancel() }
//                    b.show()
                } catch (e: Exception) {
                    Toast.makeText(activity, "Could not submit details. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        })
        return view
    }
    @SuppressLint("Range")
    private fun setUpViews() {

//        Log.i("TAG4", "setUpViews: ");
        val dp = dataBaseFoodHelper!!.readableDatabase
        val c = dp.query(FOOD_HELPER_DB_NAME_LOG, null, null, null, null, null, null)
        while (c.moveToNext()) {
            quantity = java.lang.Float.valueOf(c.getString(c.getColumnIndex(FOOD_DB_QUANTITY)))
            fat += (java.lang.Float.valueOf(c.getString(c.getColumnIndex(FAT))) * java.lang.Float.valueOf(
                quantity
            ))
            carbs += (java.lang.Float.valueOf(c.getString(c.getColumnIndex(CARBS))) * java.lang.Float.valueOf(
                quantity
            ))
            proteins += (java.lang.Float.valueOf(c.getString(c.getColumnIndex(PROTEIN))) * java.lang.Float.valueOf(
                quantity
            ))
        }
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager =
                activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    private fun CheckExist() {

//        myRef2.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (d in dataSnapshot.children ) {
//                    Log.e("k", "Check1dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"+d);
//
//                    println("dkfdkfdfkdfdfkdkfdkkWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"+d)
//                    var data=d.getValue() as HashMap<*,*>
//
//                    val firebaseUserInfo = FirebaseUserInfo(data["B"].toString().toDouble() ,data["A"].toString())
//
////                    Toast.makeText(activity,"dkddkkdkdkd ${data["A"]}",Toast.LENGTH_SHORT).show()
////                    val firebaseUserInfo=FirebaseUserInfo(d.value.a)
////                    Log.e("k", "Check1dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
//                    exercise_name.add(firebaseUserInfo.a.toString())
//                    key_here.add(d.key.toString())
//                }
////                Toast.makeText(activity,"dkddkkdkdkd ${exercise_name.size}",Toast.LENGTH_SHORT).show()
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        var exercise_context: Context? = null

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Exercise.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Exercise {
            val fragment = Exercise()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}