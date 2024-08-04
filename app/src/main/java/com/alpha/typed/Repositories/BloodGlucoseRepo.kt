package com.alpha.typed.Repositories

import android.util.Log
import android.widget.Toast
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Models.BloodGlucoseEntry
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityMainScreenBinding
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class BloodGlucoseRepo {

    fun postBloodGlucose(loading: Loading<ActivityMainScreenBinding>, email:String, value:Double, bg:String, time:String, date: Date){
        var bgEntry= BloodGlucoseEntry(email,value,bg,time,date)
        var api= RetrofitHelper.getInstance().create(com.alpha.typed.RetroInterfaces.BloodGlucose::class.java)

        api.postBgEntry(bgEntry)?.enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                loading.stopLoading()
                var str=response.body()?.string()
                val jsonObject: JsonObject =
                    JsonParser().parse(str).getAsJsonObject()
                var xx=jsonObject.get("msg")
//                Toast.makeText(activity!!,""+jsonObject.get("msg") , Toast.LENGTH_LONG).show()
                if((jsonObject.get("msg").toString()).equals("\"Nahi tha re User\"")){
//                                        loading.stopLoading()
                }
                else{
                    //                                        startActivity(Intent(requireContext(), MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        requireContext().finish()

                }

            }

//                                    val alertDialog = AlertDialog.Builder(
//                                        activity
//                                    ).create()
//                                    alertDialog.setCancelable(true)
//                                    val layoutInflater = LayoutInflater.from(activity)
//                                    val convertView: View =
//                                        layoutInflater.inflate(R.layout.custom_dialogue_box, null, true)
//                                    alertDialog.setTitle("Exercise List")
//                                    lv = convertView.findViewById<View>(R.id.lv) as ListView
//                                    val adapter = ArrayAdapter(
//                                        activity, android.R.layout.simple_list_item_1, exercise_name
//                                    )
//                                    lv.adapter = adapter
//                                    lv.onItemClickListener =
//                                        AdapterView.OnItemClickListener { adapterView, view, i, l ->
//                                            val selectedFromList = lv.getItemAtPosition(i).toString()
//                                            //                            Log.i("Check2", selectedFromList + "  " + i);
//                                            exercise_id = i
//                                            exercise = selectedFromList
//                                            exercise_text.setText(exercise)
//                                            alertDialog.cancel()
//                                        }
//                                    alertDialog.setView(convertView)
////                if (!exercise_name.isEmpty())
//                                    alertDialog.show()



            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loading.stopLoading()

                Log.e("gee",""+t.stackTrace)
//                    Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()

                Log.e("gee","onFailureeeee"+t.message)

                // handle the failure
            }
        })

    }
}