package com.alpha.typed.Models

data class PredictionTrainedParamsFetch(
    var email:String,
    var average_breakfast:Double,
    var average_lunch:Double,
    var average_snack:Double,
    var average_dinner:Double,
    var breakfast_icr:Double,
    var breakfast_isf:Double,
    var lunch_icr:Double,
    var lunch_isf:Double,
    var snack_icr:Double,
    var snack_isf:Double,
    var dinner_icr:Double,
    var dinner_isf:Double,
    var icr:Double,
    var isf:Double,
    var division_by:Double,
    var insulin_dose:Double,
    var prev_insulin_time:String,

)
