package com.alpha.typed.Navigation_drawer

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.alpha.typed.Classes.Constants
import com.alpha.typed.Classes.Constants.DIRECT_INSULIN_SCREEN
import com.alpha.typed.Classes.Constants.KEY
import com.alpha.typed.Classes.Constants.PERSON_NAME
import com.alpha.typed.Fragments.BloodGlucose
import com.alpha.typed.Fragments.Contact
import com.alpha.typed.Fragments.Exercise
import com.alpha.typed.Fragments.Food
import com.alpha.typed.Fragments.FoodAddFrag
import com.alpha.typed.Fragments.Insulin
import com.alpha.typed.Fragments.Piechart
import com.alpha.typed.Fragments.Predict
import com.alpha.typed.R
import com.alpha.typed.databinding.ActivityMainScreenBinding
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainScreen : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    Predict.OnFragmentInteractionListener,
    Piechart.OnFragmentInteractionListener,
    BloodGlucose.OnFragmentInteractionListener,
    Contact.OnFragmentInteractionListener, Food.OnFragmentInteractionListener,
    Insulin.OnFragmentInteractionListener,
    Exercise.OnFragmentInteractionListener, FoodAddFrag.OnFragmentInteractionListener

{
    var toolbar: Toolbar? = null
    lateinit var bottomNav : BottomNavigationView
    override fun onResume() {
        super.onResume()
        val i = intent
        val check_insulin = i.getStringExtra(DIRECT_INSULIN_SCREEN)
        if (check_insulin != null && check_insulin == "insulin") {
            toolbar!!.setTitle("Insulin")
            val insulinFragment = Insulin()
            bottomNav.selectedItemId=R.id.insulin
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction()
            supportFragmentManager.beginTransaction().replace(R.id.app_bar, insulinFragment, insulinFragment.tag).commit()
        } else if (check_insulin != null && check_insulin == "predict") {
            toolbar!!.setTitle("Prediction")
            val predict = Predict()
            bottomNav.selectedItemId=R.id.prediction

            val fragmentManager = fragmentManager
            supportFragmentManager.beginTransaction().replace(R.id.app_bar, predict, predict.tag).commit()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        toolbar = findViewById<View>(com.alpha.typed.R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar!!.setTitle("App name")
        title = "Exercise"
        toolbar?.setTitleTextColor(getResources().getColor(com.alpha.typed.R.color.black))

        var tts=TapTargetSequence(this)
            .targets(
                TapTarget.forView(
                    findViewById(R.id.toolbar),
                    "Welcome to T1 Life!! ",
                    "This entails a swift overview of the application's features. Familiarize yourself with its usage intricacies by attentively perusing each step."
                )
                    .cancelable(false)
                    .outerCircleColor(R.color.tour)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(30)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.white)      // Specify the color of the title text
                    .descriptionTextSize(20)
                    .descriptionTextColor(R.color.white)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .textTypeface(Typeface.SANS_SERIF)
                    .transparentTarget(true)
                    .targetRadius(60),
                TapTarget.forView(
                    findViewById<View>(R.id.daily_log),
                    "Home Tab",
                    "In this tab, you can view various activities entered for the day."
                )
                    .cancelable(false)
                    .outerCircleColor(R.color.tour)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .targetRadius(60)
                    .titleTextSize(30)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.white)      // Specify the color of the title text
                    .descriptionTextSize(20)
                    .descriptionTextColor(R.color.white)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .textTypeface(Typeface.create("Balsamiq Sans", R.font.balsamiq_sans))
                    .transparentTarget(true),

                TapTarget.forView(
                    findViewById<View>(R.id.exercise),
                    "Exercise Tab",
                    "Enter details of any physical activity performed in this tab."
                )
                    .cancelable(false)
                    .outerCircleColor(R.color.tour)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(30)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.white)      // Specify the color of the title text
                    .descriptionTextSize(20)
                    .descriptionTextColor(R.color.white)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .textTypeface(Typeface.create("Balsamiq Sans", R.font.balsamiq_sans))
                    .targetRadius(60)

                    .transparentTarget(true),
                TapTarget.forView(
                    findViewById<View>(R.id.prediction),
                    "Prediction/Food Tab",
                    "This tab provides insulin dose predictions based on entered food intake. Initially, predictions won't be available, but input your food data four times daily. After a few days, insulin predictions will be displayed."
                )
                    .cancelable(false)
                    .outerCircleColor(R.color.tour)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(30)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.white)      // Specify the color of the title text
                    .descriptionTextSize(20)
                    .descriptionTextColor(R.color.white)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .tintTarget(true)
                    .transparentTarget(true)
                    .targetRadius(60)
                    .textTypeface(Typeface.create("Balsamiq Sans", R.font.balsamiq_sans))
                    .transparentTarget(true),
                TapTarget.forView(
                    findViewById<View>(R.id.insulin),
                    "Insulin Tab",
                    "Input your insulin dosage in this tab everytime you take it."
                )
                    .cancelable(false)
                    .outerCircleColor(R.color.tour)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(30)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.white)      // Specify the color of the title text
                    .descriptionTextSize(20)
                    .descriptionTextColor(R.color.white)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .tintTarget(true)
                    .targetRadius(60)

                    .transparentTarget(true)
                    .textTypeface(Typeface.create("Balsamiq Sans", R.font.balsamiq_sans))
                    .transparentTarget(true),
                TapTarget.forView(
                    findViewById<View>(R.id.bg_value),
                    "Blood Glucose(BG) Tab",
                    "Record your blood glucose measurement here each time you check it."
                )
                    .cancelable(false)
                    .outerCircleColor(R.color.tour)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(30)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.white)      // Specify the color of the title text
                    .descriptionTextSize(20)
                    .descriptionTextColor(R.color.white)
                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .targetRadius(60)

                    .tintTarget(true)
                    .transparentTarget(true)
                    .textTypeface(Typeface.create("Balsamiq Sans", R.font.balsamiq_sans))
                    .transparentTarget(true),
            )
            .listener(object : TapTargetSequence.Listener {
                // This listener will tell us when interesting(tm) events happen in regards
                // to the sequence
                override fun onSequenceFinish() {
                    // Yay
                }

                override fun onSequenceStep(lastTarget: TapTarget, targetClicked: Boolean) {
                    // Perform action for the current target
                }

                override fun onSequenceCanceled(lastTarget: TapTarget) {
                    // Boo
                }
            })
        //        val arrowImageView = findViewById<ImageView>(R.id.arrowImageView)
//
//        // Animate the arrow pointer
//        val animator = ObjectAnimator.ofFloat(arrowImageView, "translationY", 0f, 50f).apply {
//            duration = 1000 // Animation duration in milliseconds
//            repeatCount = ObjectAnimator.INFINITE // Repeat animation indefinitely
//            repeatMode = ObjectAnimator.REVERSE // Reverse the animation direction
//            interpolator = AccelerateDecelerateInterpolator() // Apply acceleration and deceleration to the animation
//        }
//
//        // Start the animation
//        animator.start()
//

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        toolbar!!.setNavigationIcon(R.drawable.menu)
        toggle.setHomeAsUpIndicator(R.drawable.menu)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
//        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
//        getSupportActionBar()?.setHomeButtonEnabled(true);
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView

//        var mAuth=FirebaseAuth.getInstance()
        var header=navigationView.getHeaderView(0)
        var userName=header.findViewById<TextView>(R.id.user_name)
        var userEmail=header.findViewById<TextView>(R.id.user_email)
        var p = PreferenceManager.getDefaultSharedPreferences(this@MainScreen)
        var name=p.getString(PERSON_NAME,null)
        var email=p.getString(KEY ,null)

//        val user: FirebaseUser = mAuth!!.getCurrentUser()!!
//        1bLb0BS9LdMqJF69roCMT4WeNYK2
        userName.setText(name)
        userEmail.setText(email)
//        println("UDIFDOFDFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"+mAuth.uid)

        navigationView.setNavigationItemSelectedListener(this)
        val i = intent
        val check_insulin = i.getStringExtra(DIRECT_INSULIN_SCREEN)
        if (check_insulin != null && check_insulin == "insulin") {
            toolbar!!.setTitle("Insulin")
            val insulinFragment = Insulin()
            val fragmentManager = fragmentManager
            supportFragmentManager.beginTransaction()
                .replace(R.id.app_bar, insulinFragment, insulinFragment.tag).commit()
        } else if (check_insulin != null && check_insulin == "predict") {
            toolbar!!.setTitle("Prediction")
            val predict = Predict()
            val fragmentManager = fragmentManager
            supportFragmentManager.beginTransaction().replace(R.id.app_bar, predict, predict.tag).commit()
        } else {
            drawer.openDrawer(Gravity.LEFT)
        }

        bottomNav=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {
            val id = it.itemId
            if (id == R.id.insulin) {
                toolbar?.setTitle("Insulin")
                val insulinFragment = Insulin()
                val fragmentManager = fragmentManager
                supportFragmentManager.beginTransaction()
                    .replace(R.id.app_bar, insulinFragment, insulinFragment.tag).commit()
                true
            }
            if (id == R.id.exercise) {
                toolbar?.setTitle("Exercise")
                val exerciseFragment = Exercise()
                val fragmentManager = fragmentManager
                supportFragmentManager.beginTransaction().replace(R.id.app_bar, exerciseFragment, exerciseFragment.tag).commit()
                true
            }
            if (id == R.id.bg_value) {
                toolbar?.setTitle("Blood Glucose")
                val bloodGlucose = BloodGlucose()
                val fragmentManager = fragmentManager
                supportFragmentManager.beginTransaction().replace(R.id.app_bar, bloodGlucose, bloodGlucose.tag)
                    .commit()
                true
            }
            if (id == R.id.daily_log) {
                toolbar?.setTitle("Home")
                val pieFragment = Piechart()
                val fragmentManager = fragmentManager
//                fragmentManager.beginTransaction().replace(R.id.app_bar, pieFragment, pieFragment.tag).commit()
                supportFragmentManager.beginTransaction().replace(R.id.app_bar,pieFragment,pieFragment.tag).commit()
                true
            }
            if (id == R.id.prediction) {
                toolbar?.setTitle("Prediction")
                val predict = Predict()
                val fragmentManager = fragmentManager
                supportFragmentManager.beginTransaction().replace(R.id.app_bar, predict, predict.tag)
                    .commit()

                true
            }
            else{
                true
            }
        }
        bottomNav.selectedItemId=R.id.exercise
        drawer.closeDrawer(GravityCompat.START)
        if(PreferenceManager.getDefaultSharedPreferences(this@MainScreen).getBoolean(Constants.SHOW_ONBOARDING_TOUR,true)){
            tts.start()
        }
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@MainScreen)
        val editor = preferences.edit()
        editor.putBoolean(Constants.SHOW_ONBOARDING_TOUR, false)
        editor.apply()
        editor.commit()

    }

    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
//        if (id == R.id.insulin) {
//            toolbar?.setTitle("Insulin")
//            val insulinFragment = Insulin()
//            val fragmentManager = fragmentManager
//            fragmentManager.beginTransaction()
//                .replace(R.id.app_bar, insulinFragment, insulinFragment.tag).commit()
//        } else if (id == R.id.exercise) {
//            toolbar?.setTitle("Exercise")
//            val exerciseFragment = Exercise()
//            val fragmentManager = fragmentManager
//            fragmentManager.beginTransaction()
//                .replace(R.id.app_bar, exerciseFragment, exerciseFragment.tag).commit()
//        } else if (id == R.id.bg_value) {
//            toolbar?.setTitle("Blood Glucose")
//            val bloodGlucose = BloodGlucose()
//            val fragmentManager = fragmentManager
//            fragmentManager.beginTransaction().replace(R.id.app_bar, bloodGlucose, bloodGlucose.tag)
//                .commit()
//        }
        if (id == R.id.contact_us) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("yashbansal1011@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "<Write your subject here>")
            intent.putExtra(Intent.EXTRA_TEXT, "<Write your query here>")
            startActivity(Intent.createChooser(intent, ""))        }

//         else if (id == R.id.pie_chart) {
//            toolbar?.setTitle("Daily log")
////            val pieFragment = Piechart()
////            val fragmentManager = fragmentManager
////            fragmentManager.beginTransaction().replace(R.id.app_bar, pieFragment, pieFragment.tag)
////                .commit()
//        }
//        else if (id == R.id.prediction) {
//            toolbar?.setTitle("Prediction")
//            val predict = Predict()
//            val fragmentManager = fragmentManager
//            fragmentManager.beginTransaction().replace(R.id.app_bar, predict, predict.tag).commit()
//        }
//        else if (id == R.id.log_out) {
//            val b = AlertDialog.Builder(this@MainScreen)
//            b.setTitle("Alert")
//            b.setMessage("Are you sure you want to logout ?")
//            b.setPositiveButton(
//                "Yes"
//            ) { dialogInterface, index ->
//                FirebaseAuth.getInstance().signOut()
//                signOut()
//                startActivity(Intent(this@MainScreen, Login::class.java))
//                dialogInterface.cancel()
//            }
//            b.setNegativeButton(
//                "No"
//            ) { dialogInterface, i -> dialogInterface.cancel() }
//            b.show()
//        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun signOut() {
//        Login.mGoogleSignInClient?.signOut()
//            ?.addOnCompleteListener(this, object : OnCompleteListener<Void?> {
//                override fun onComplete(task: Task<Void?>) {
//                    this@MainScreen.finish()
//                }
//            })
    }

    override fun onFragmentInteraction(uri: Uri?) {}
}