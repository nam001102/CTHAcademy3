package com.cthacademy.android.modal

import java.io.Serializable

data class CoreInformation(
    val Name:String,
    val Date:String,
    val Life:Int,
    var Destiny:Int = 0,
    var Connection:Int = 0,
    var Growth:Int = 0,
    var Soul:Int = 0,
    var Personality:Int = 0,
    var Connecting:Int = 0,
    var Balance:Int = 0,
    var RationalThinking:Int = 0,
    var MentalPower:Int = 0,
    var DayOfBirth:Int = 0,
    var Passion: List<Int>,
    var MissingNumbers: List<Int>,
    var PersonalYearSum:Int = 0,
    var PersonalMonthSum:Int = 0,
    var PersonalDaySum:Int = 0,
    var Phrase1:Int = 0,
    var Phrase2:Int = 0,
    var Phrase3:Int = 0,
    var Phrase4:Int = 0,
    var Challange1:Int = 0,
    var Challange2:Int = 0,
    var Challange3:Int = 0,
    var Challange4:Int = 0,
    var Agging1:Int = 0,
    var Agging2:Int = 0,
    var Agging3:Int = 0,
    var Agging4:Int = 0,
) : Serializable

