package com.alpha.typed.Models

import java.util.Date

data class PredictionExtraDetails(
    var email:String,
    var current_bg:Double,
    var date:Date,
    var time:String,
    var food_category:String,
    var food_quantity:Double,
    var food_name:String
)
