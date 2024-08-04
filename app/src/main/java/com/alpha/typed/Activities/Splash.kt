package com.alpha.typed.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alpha.typed.R
import com.alpha.typed.databinding.ActivitySplashBinding


class Splash : AppCompatActivity() {
    lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        var view=binding.root
        setContentView(view)
        supportActionBar?.hide()
        shineAnimation()

        Handler().postDelayed({
            val i = Intent(this@Splash, Login::class.java)
            startActivity(i)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    private fun shineAnimation() {
        // attach the animation layout Using AnimationUtils.loadAnimation
        val anim = AnimationUtils.loadAnimation(this, R.anim.left_right)
        binding.shine .startAnimation(anim)
        // override three function There will error
        // line below the object
        // click on it and override three functions
        anim.setAnimationListener(object : Animation.AnimationListener {
            // This function starts the
            // animation again after it ends
            override fun onAnimationEnd(p0: Animation?) {
                val layoutParams: ViewGroup.LayoutParams = binding.shine.getLayoutParams()
                layoutParams.width = 0
                binding.shine.setLayoutParams(layoutParams)
            }

            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

        })
    }

    companion object {
        var SPLASH_TIME_OUT = 1000
    }
}