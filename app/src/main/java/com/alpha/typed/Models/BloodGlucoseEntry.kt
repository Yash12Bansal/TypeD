package com.alpha.typed.Models

import java.util.Date

data class BloodGlucoseEntry(
    var email:String,
    var value:Double,
    var type:String,
    var time:String,
    var date:Date
)
