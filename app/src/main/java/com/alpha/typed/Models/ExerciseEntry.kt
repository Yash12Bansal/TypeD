package com.alpha.typed.Models

import java.sql.Time
import java.util.Date


data class ExerciseEntry(
    var email:String,
    var currentTime:String,
    var endTime:String,
    var startTime:String,
    var date: Date,
    var exercise:String

    )
