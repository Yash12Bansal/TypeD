package com.alpha.typed.Models

import java.util.Date

data class PredictionValuesStatus(
    var email:String,
    var amount:Double,
    var date:Date,
    var time:String,
    var status:String
)
