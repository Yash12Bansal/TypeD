package com.alpha.typed.Utils

import android.app.Activity
import android.content.Context
import android.renderscript.ScriptGroup.Binding
import android.view.View
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import androidx.viewbinding.ViewBindings
import com.alpha.typed.databinding.ActivityEnterDetailsBinding
import com.alpha.typed.databinding.ActivityLoginBinding
class Loading<T>(var activity: Activity,var rotateloading:View,var screen:View){
    private var shortAnimationDuration: Int = 0

    init{
        shortAnimationDuration = activity.resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    fun startLoading(){
        rotateloading?.visibility= View.VISIBLE
        activity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        screen.apply{
            alpha = 0.8f
            animate()
                .alpha(0.8f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)

        }

    }

    fun stopLoading(){
        rotateloading?.visibility= View.INVISIBLE
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        screen.apply{
            alpha = 0.8f
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)

        }
    }
}