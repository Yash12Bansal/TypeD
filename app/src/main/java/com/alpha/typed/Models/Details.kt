package com.alpha.typed.Models

import java.util.Date

data class Details(
    var email:String,
    var name:String,
    var phone:String,
    var gender:String,
    var dob:Date,
    var weight:Double,
    var height:Double,
    var doctorName:String,
    var doses:Double,
    var yearOfDiagnosis:Date
)
