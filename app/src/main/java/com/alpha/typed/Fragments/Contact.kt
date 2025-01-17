package com.alpha.typed.Fragments
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.alpha.typed.R
import com.alpha.typed.Classes.Constants.PERSON_NAME

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Contact.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Contact.newInstance] factory method to
 * create an instance of this fragment.
 */
class Contact : Fragment() {
    var name: TextView? = null
    var description: EditText? = null
    var submit: CardView? = null
//    var database = FirebaseDatabase.getInstance()
//    var myRef = database.getReference("contact_us")

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_contact, container, false)
        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
        submit = view.findViewById(R.id.send)
        val preferences = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        val person_name = preferences.getString(PERSON_NAME, null)
        name?.setText(person_name)
        submit?.setOnClickListener(View.OnClickListener {
            try {
                val b = AlertDialog.Builder(
                    activity
                )
                b.setTitle("Alert")
                b.setMessage("Are you sure you want to submit ?")
                b.setPositiveButton(
                    "Yes"
                ) { dialogInterface, index ->
                    if ("" == description?.getText().toString()) {
                        Toast.makeText(activity, "Enter description", Toast.LENGTH_SHORT).show()
                    } else {
                        val emailIntent = Intent(
                            Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "docrajivsingla@gmail.com", null
                            )
                        )
                        emailIntent.putExtra(
                            Intent.EXTRA_SUBJECT,
                            "query from $person_name"
                        )
                        emailIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            description?.getText().toString()
                        )
                        emailIntent.putExtra(
                            Intent.EXTRA_EMAIL,
                            arrayOf(
                                "samwilson14111@gmail.com",
                                "docrajivsingla@gmail.com",
                            )
                        )
                        activity.startActivity(Intent.createChooser(emailIntent, null))
                        description?.setText("")
                    }
                    dialogInterface.cancel()
                }
                b.setNegativeButton(
                    "No"
                ) { dialogInterface, i -> dialogInterface.cancel() }
                b.show()
            } catch (e: Exception) {
                Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
        return view
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

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Contact.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Contact {
            val fragment = Contact()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}