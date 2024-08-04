package com.alpha.typed.Activities

import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.alpha.typed.Classes.Constants.CALORIES
import com.alpha.typed.Classes.Constants.CARBS
import com.alpha.typed.Classes.Constants.DB_CATEGORY_ENTERED
import com.alpha.typed.Classes.Constants.FAT
import com.alpha.typed.Classes.Constants.FOOD_DB_NAME
import com.alpha.typed.Classes.Constants.FOOD_DB_QUANTITY
import com.alpha.typed.Classes.Constants.FOOD_DB_TIME_STAMP
import com.alpha.typed.Classes.Constants.FOOD_HELPER_DB_ID_LOG
import com.alpha.typed.Classes.Constants.FOOD_HELPER_DB_NAME_LOG
import com.alpha.typed.Classes.Constants.PROTEIN
import com.alpha.typed.Classes.Constants.USER_DATE_ENTERED
import com.alpha.typed.Classes.Constants.USER_TIME_ENTERED

class DataBaseFoodHelperLog(context: Context?) :

    SQLiteOpenHelper(context, "FoodHelperLog", null, 1) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val createtable =
            (" CREATE TABLE " + FOOD_HELPER_DB_NAME_LOG.toString() + " ( " + FOOD_HELPER_DB_ID_LOG.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FOOD_DB_NAME.toString() + " TEXT, " + FOOD_DB_TIME_STAMP.toString() + " TEXT, " + FOOD_DB_QUANTITY.toString() + " TEXT, " + USER_DATE_ENTERED.toString() + " TEXT, " + USER_TIME_ENTERED.toString() + " TEXT," + DB_CATEGORY_ENTERED.toString() + " TEXT,  " + FAT.toString() + " TEXT," + CARBS.toString() + " TEXT," + PROTEIN.toString() + " TEXT," + CALORIES.toString() + " TEXT); ")
        sqLiteDatabase.execSQL(createtable)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
}