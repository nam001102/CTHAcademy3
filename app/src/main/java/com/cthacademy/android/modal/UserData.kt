package com.cthacademy.android.modal

import java.io.Serializable

data class UserData(
    val avatar : String? = null,
    val phone : String? = null,
    val name : String? = null,
    val date : String? = null,
    val point : Long? = 0,
    val Life : Boolean = true,
    var Destiny : Boolean = true,
    var Connection : Boolean = false,
    var Growth : Boolean = false,
    var Soul : Boolean = false,
    var Personality : Boolean = false,
    var Connecting : Boolean = false,
    var Balance : Boolean = false,
    var RationalThinking : Boolean = false,
    var MentalPower : Boolean = false,
    var DayOfBirth : Boolean = false,
    var Passion : Boolean = false,
    var MissingNumbers : Boolean = false,
    var PersonalYear : Boolean = false,
    var PersonalMonth : Boolean = false,
    var PersonalDay : Boolean = false,
    var Phrase : Boolean = false,
    var Challange : Boolean = false,
    var Agging : Boolean = false,

) : Serializable
