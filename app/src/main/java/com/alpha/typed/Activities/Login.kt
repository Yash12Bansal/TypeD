package com.alpha.typed.Activities

import android.content.Intent
import android.content.IntentSender
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alpha.typed.Classes.Constants.KEY
import com.alpha.typed.Classes.Constants.PERSON_NAME
import com.alpha.typed.Config.RetrofitHelper
import com.alpha.typed.Models.User
import com.alpha.typed.Navigation_drawer.MainScreen
//import com.alpha.typed.Navigation_drawer.MainScreen
import com.alpha.typed.R
import com.alpha.typed.RetroInterfaces.UserLogin
import com.alpha.typed.Utils.Loading
import com.alpha.typed.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
//    lateinit var googleIdOptions: GetGoogleIdOption
    var REQ_ONE_TAP=1
    var TAG="LOGIN"
    lateinit var loading: Loading<ActivityLoginBinding>
    lateinit var api :UserLogin
    override fun onStart() {
        super.onStart()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.rotateloading?.visibility=View.INVISIBLE
        loading= Loading<ActivityLoginBinding>(this,binding.rotateloading,binding.screen)
        api= RetrofitHelper.getInstance().create(UserLogin::class.java)
//        googleIdOptions=GetGoogleIdOption.Builder()
//            .setServerClientId(getString(R.string.web_client_id))
//        .build()
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        binding.signInButton!!.setOnClickListener(View.OnClickListener {
            if (isNetworkAvailable) {
                loading.startLoading()
                signIn()
            } else {
                Toast.makeText(this@Login, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    var personName=credential.displayName
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
//                            Log.d(TAG, "Got ID token.")
                            var user=User(idToken,username)
//                            GlobalScope.launch {
//                                Log.d("hello","kllkkljkljlkjlkjkjkjkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk")
//                                val result = api.auth()
//                                if (result != null){
//                                    Log.d("hello","KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"+result)
//                                }
////                                    Toast.makeText(this@Login,result.toString(),Toast.LENGTH_LONG).show()
//
//                                // Checking the results
////                                    Log.d("ayush: ", result.body().toString())
//                            }
                            api.auth(user)?.enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Login)
                                    val editor = preferences.edit()
                                    editor.putString(KEY, username)
                                    editor.putString(PERSON_NAME, personName)
                                    editor.apply()
                                    editor.commit()
                                    var str=response.body()?.string()
                                    val jsonObject: JsonObject =
                                        JsonParser().parse(str).getAsJsonObject()
                                    var xx=jsonObject.get("msg")
//                                    Toast.makeText(this@Login,""+jsonObject.get("msg") ,Toast.LENGTH_LONG).show()
                                    if((jsonObject.get("code").toString()).equals("2")){
                                        loading.stopLoading()
                                        startActivity(Intent(this@Login, EnterDetails::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                                        this@Login.finish()
                                    }
                                    else{
                                        loading.stopLoading()

//                                        startActivity(
//                                            Intent(this@Login, RequestSentActivity::class.java).addFlags(
//                                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        this@Login.finish()
                                        Log.e("this",jsonObject.toString())
                                        if (str != null) {
                                            Log.e("tjs",str)
                                        }
                                        Log.e("DF","fkdfkjfkdfjkfkjdfjkdfkjdfjkdjfkdkjfdjkf"+jsonObject.get("code").toString())
                                        if((jsonObject.get("code").toString()).equals("2") || (jsonObject.get("code").toString()).equals("3")){

//                if((jsonObject.get("code").toString()).equals("\"Nahi tha re User\"")){

                                            startActivity(
                                                Intent(this@Login, EnterDetails::class.java).addFlags(
                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                                            this@Login.finish()
                                        }
                                        else if((jsonObject.get("code").toString()).equals("1")){
                                            startActivity(
                                                Intent(this@Login, MainScreen::class.java).addFlags(
                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                                            this@Login.finish()
                                        }
                                        else if((jsonObject.get("code").toString()).equals("0")){
                                            startActivity(
                                                Intent(this@Login, RequestSentActivity::class.java).addFlags(
                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                                            this@Login.finish()

                                        }

                                        else{
                                            loading.stopLoading()
                                            Toast.makeText(this@Login,"Network Issue\nTry Again",Toast.LENGTH_LONG)
                                        }

//                                        startActivity(
//                                            Intent(this@Login, MainScreen::class.java).addFlags(
//                                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        this@Login.finish()

//                                        startActivity(Intent(this@Login, EnterDetails::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        this@Login.finish()

                                    //                                        startActivity(Intent(this@Login, MainScreen::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                        this@Login.finish()

                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    loading.stopLoading()

                                    Log.e("gee",""+t.stackTrace)
                                    Toast.makeText(this@Login,t.message,Toast.LENGTH_LONG).show()
                                    Log.e("gee","onFailureeeee"+t.message)
                                }
                            })
                            Log.e("this","JFDIFJDIFJIDFIDFIJI")
//                            loading.stopLoading()
                        }

                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    loading.stopLoading()

                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
//                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(TAG, "Couldn't get credential from result." +
                                    " (${e.localizedMessage})")
                        }
                    }
                }
            }
        }    }
    private fun signIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    Toast.makeText(this,"Starting sign in process...",Toast.LENGTH_LONG).show()
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    loading.stopLoading()
                    Toast.makeText(this,"Couldn't start One Tap UI\nTry again",Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                loading.stopLoading()

                oneTapClient.beginSignIn(signUpRequest)
                    .addOnSuccessListener(this) { result ->
                        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()

                        try {
                            startIntentSenderForResult(
                                result.pendingIntent.intentSender, REQ_ONE_TAP,
                                null, 0, 0, 0)
                        } catch (e: IntentSender.SendIntentException) {
                            Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                        }
                    }
                    .addOnFailureListener(this) { e ->
                        Toast.makeText(this,"No accounts found for Google sign up",Toast.LENGTH_LONG).show()
                        // No Google Accounts found. Just continue presenting the signed-out UI.
                        Log.e(TAG,"THIS IS THE ERROR NO GOOGLE ACCOUNTS "+ e.localizedMessage)
                    }
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
            }
        //        val signInIntent = mGoogleSignInClient!!.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
//    companion object {
//        private const val RC_SIGN_IN = 2
//        var mGoogleSignInClient: GoogleSignInClient? = null
//    }

}