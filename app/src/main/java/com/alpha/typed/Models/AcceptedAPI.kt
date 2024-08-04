package com.alpha.typed.Models
import java.util.Date

data class AcceptedAPI(
    var email:String,
    var amount:Double,
    var date:Date,
    var time:String,
    var status:String,
    var insulin_dose:Double,
    var prev_insulin_time:String,
)
