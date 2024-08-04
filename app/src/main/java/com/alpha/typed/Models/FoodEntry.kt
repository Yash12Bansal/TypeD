package com.alpha.typed.Models

import java.util.Date

data class FoodEntry(
    var email:String,
    var date:Date,
    var time:String,
    var food_name:String,
    var food_category:String,
    var food_quantity:Double
)
