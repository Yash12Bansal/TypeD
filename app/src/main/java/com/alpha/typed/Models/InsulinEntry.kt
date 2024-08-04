package com.alpha.typed.Models

import java.time.temporal.TemporalAmount
import java.util.Date

data class InsulinEntry(
    var email:String,
    var amount:Double,
    var category:String,
    var correction_dose:Double,
    var date:Date,
    var time:String,
    var type:String
)
