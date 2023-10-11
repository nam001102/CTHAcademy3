package com.cthacademy.android.custom

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import com.cthacademy.android.modal.CoreInformation
import java.text.ParseException
import java.util.*

@SuppressLint("SimpleDateFormat")
private fun test(name: String, date: String): CoreInformation {

    val format = SimpleDateFormat("dd/MM/yyyy")


    var Life = 0

    //Life
    if (isValidDate(date)) {
        format.parse(date)
        val dateArray = date.split("/").map {
            it.toInt()
        }.toIntArray()
        val splitArrays = reduceAndSum(dateArray)

        Life = splitArrays

//        val arraySums = reduceAndSum(splitArrays)
//        for (num in arraySums) {
//            Life += num
//        }
//        while (Life !in setOf(11, 22, 33)) {
//            if (Life >= 10) {
//                Life = Life.toString().map { it.toString().toInt() }.sum()
//            } else {
//                break
//            }
//        }
    }

    //Destiny
    val Destiny = calculatePythagoreanValue(upperCaseToLowerCase(name))


    //Connection
    val Connection = absoluteValue(Destiny - Life)

    //Growth
    var Growth = Destiny + Life
    if (Growth !in setOf(11, 22, 33)) {
        Growth = separateAndSum(Growth)
    }

    //Soul
    val Soul = calculatePythagoreanVowels(upperCaseToLowerCase(name))

    //Personality
    val Personality = calculatePythagoreanNonVowels(upperCaseToLowerCase(name))

    //Connecting Soul and Personality
    val  Connecting = absoluteValue(Soul - separateAndSum(Personality))


    //Balance
    val Balance = getFirstChar(upperCaseToLowerCase(name).split(" ").toTypedArray())

    //Rational Thinking
    val last = getLastString(upperCaseToLowerCase(name).split(" ").toTypedArray())
    val first = getFirstString(upperCaseToLowerCase(date).split("/").toTypedArray())

    var RationalThinking = last + first

    if (RationalThinking !in setOf(11, 22, 33)) {
        val temp = separateAndSum(RationalThinking)
        RationalThinking = temp
    }


    //Mental Power
    val MentalPower = 9 - countMissingNumbers(
        upperCaseToLowerCase(name).replace(" ", "")
            .map { letter -> pythagoreanValue(letter.toString()) }.toIntArray()
    )

    //Day Of Birth
    val DayOfBirth = getDayOfBirth(date)
    //Passion
    val Passion = getMostRepeatedNumbers(
        upperCaseToLowerCase(name).replace(" ", "")
            .map { letter -> pythagoreanValue(letter.toString()) }.toIntArray()
    )

    //Missing Numbers
    val MissingNumbers = getMissingNumbers(
        upperCaseToLowerCase(name).replace(" ", "")
            .map { letter -> pythagoreanValue(letter.toString()) }.toIntArray()
    )

    //Personal Year
    val PersonalYear = reduceAndSum(getDayMonth(date)) + reduceYearToSingleDigit()
    var PersonalYearSum = 0
    PersonalYearSum = separateAndSum(PersonalYear)

    //Personal Month
    val PersonalMonth = getYear(date) + getNowMonth()
    var PersonalMonthSum = 0

    PersonalMonthSum = separateAndSum(PersonalMonth)

    //Personal Day
    val PersonalDay = getMonth(date) + getNowDay()
    var PersonalDaySum = 0

    PersonalDaySum = separateAndSum(PersonalDay)


    //Phrase:
    //Phrase 1
    val Phrase1 = reduceAndSum(getDayMonth(date))

    //Phrase 2
    val Phrase2 = reduceAndSum(getDayYear(date))

    //Phrase 3
    var Phrase3 = 0
    if (Phrase1 + Phrase2 >= 10) {
        Phrase3 = separateAndSum(Phrase1 + Phrase2)
    } else {
        Phrase3 = Phrase1 + Phrase2
    }

    //Phrase 4
    val Phrase4 = reduceAndSum(getMonthYear(date))

    //Challange
    //Challange1
    val Challange1 = absoluteValue(abtractDayMonth(date))
    //Challange2
    val Challange2 = absoluteValue(abtractDayYear(date))
    //Challange3
    val Challange3 = absoluteValue(Challange1 - Challange2)
    //Challange4
    val Challange4 = absoluteValue(abtractMonthYear(date))

    //Agging
    //Agging1
    val Agging1 = 36 - Life
    //Agging2
    val Agging2 = 9 + Agging1
    //Agging3
    val Agging3 = 9 + Agging2
    //Agging4
    val Agging4 = 9 + Agging3

    return CoreInformation(
        Name = name,
        Date = date,
        Life = Life,
        Destiny = Destiny,
        Connection = absoluteValue(Connection),
        Growth = Growth,
        Soul = Soul,
        Personality = Personality,
        Connecting = absoluteValue(Connecting),
        Balance = Balance,
        RationalThinking = RationalThinking,
        MentalPower = MentalPower,
        DayOfBirth = DayOfBirth,
        Passion = Passion,
        MissingNumbers = MissingNumbers,
        PersonalYearSum = PersonalYearSum,
        PersonalMonthSum = PersonalMonthSum,
        PersonalDaySum = PersonalDaySum,
        Phrase1 = Phrase1,
        Phrase2 = Phrase2,
        Phrase3 = Phrase3,
        Phrase4 = Phrase4,
        Challange1 = Challange1,
        Challange2 = Challange2,
        Challange3 = Challange3,
        Challange4 = Challange4,
        Agging1 = Agging1,
        Agging2 = Agging2,
        Agging3 = Agging3,
        Agging4 = Agging4
    )
}
fun separateAndSum(number: Int): Int {
    var sum = 0
    var n = number

    while (n > 9) {
        sum += n % 10
        n /= 10
    }

    sum = n

    return sum
}

fun isValidDate(date: String): Boolean {
    val format = SimpleDateFormat("dd/MM/yyyy")
    format.isLenient = false

    return try {
        format.parse(date)
        true
    } catch (e: ParseException) {
        false
    }
}

fun pythagoreanValue(letter: String): Int {
    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    return when (val index = alphabet.indexOf(letter.toLowerCase())) {
        in 0..8 -> index + 1
        in 9..17 -> index - 8
        in 18..25 -> index - 17
        else -> 0
    }
}

fun upperCaseToLowerCase(str: String): String {
    return str.toLowerCase(Locale.ROOT)
        .replace("[","")
        .replace("]","")
        .replace(",","")
        .replace("á", "a")
        .replace("à", "a")
        .replace("ả", "a")
        .replace("ạ", "a")
        .replace("ã", "a")
        .replace("ă", "a")
        .replace("ắ", "a")
        .replace("ằ", "a")
        .replace("ẳ", "a")
        .replace("ẵ", "a")
        .replace("ặ", "a")
        .replace("â", "a")
        .replace("ấ", "a")
        .replace("ầ", "a")
        .replace("ẩ", "a")
        .replace("ẫ", "a")
        .replace("ậ", "a")
        .replace("đ", "d")
        .replace("é", "e")
        .replace("è", "e")
        .replace("ẻ", "e")
        .replace("ẽ", "e")
        .replace("ẹ", "e")
        .replace("ê", "e")
        .replace("ế", "e")
        .replace("ề", "e")
        .replace("ễ", "e")
        .replace("ể", "e")
        .replace("ệ", "e")
        .replace("ó", "o")
        .replace("ò", "o")
        .replace("ỏ", "o")
        .replace("õ", "o")
        .replace("ọ", "o")
        .replace("ô", "o")
        .replace("ơ", "o")
        .replace("ớ", "o")
        .replace("ờ", "o")
        .replace("ở", "o")
        .replace("ỡ", "o")
        .replace("ợ", "o")
        .replace("ú", "u")
        .replace("ù", "u")
        .replace("ũ", "u")
        .replace("ụ", "u")
        .replace("ư", "u")
        .replace("ứ", "u")
        .replace("ừ", "u")
        .replace("ử", "u")
        .replace("ữ", "u")
        .replace("ự", "u")
        .replace("í", "i")
        .replace("ì", "i")
        .replace("ĩ", "i")
        .replace("ị", "i")
        .replace("ỉ", "i")
        .replace("ý", "y")
        .replace("ỳ", "y")
        .replace("ỷ", "y")
        .replace("ỹ", "y")
        .replace("ỵ", "y")
}

fun calculatePythagoreanVowels(input: String): Int {
    val vowelsArr = input.split(" ").toTypedArray()
    val vowel = separateVowels(vowelsArr)
    val splitVowelsArrays = vowel.map {
        it.toCharArray().map { pythagoreanValue(it.toString()) }.toIntArray().sum()
    }

    val result = splitVowelsArrays.flatMap { num ->
        if (num >= 10) {
            val digits = num.toString().toCharArray().map { digit -> digit.toString().toInt() }
            listOf(digits.sum())
        } else {
            listOf(num)
        }
    }.toIntArray()
    var sum = 0
    for (num in result) {
        sum += num
    }

    while (sum !in setOf(11, 22, 33)) {
        if (sum >= 10) {
            sum = sum.toString().map { it.toString().toInt() }.sum()
        } else {
            break
        }
    }

    return sum
}

fun calculatePythagoreanNonVowels(input: String): Int {
    val nonvowelsArr = input.split(" ").toTypedArray()
    val nomvowel = separateNonVowels(nonvowelsArr)
    val splitNonVowelsArrays = nomvowel.map {
        it.toCharArray().map { pythagoreanValue(it.toString()) }.toIntArray().sum()
    }
    val result = splitNonVowelsArrays.flatMap { num ->
        if (num >= 10) {
            val digits = num.toString().toCharArray().map { digit -> digit.toString().toInt() }
            listOf(digits.sum())
        } else {
            listOf(num)
        }
    }.toIntArray()
    var sum = 0
    for (num in result) {
        sum += num
    }

    while (sum !in setOf(11, 22, 33)) {
        if (sum >= 10) {
            sum = sum.toString().map { it.toString().toInt() }.sum()
        } else {
            break
        }
    }

    return sum
}

fun separateVowels(arr: Array<String>): List<String> {
    val vowels = listOf("a", "e", "i", "o", "u")
    val result = mutableListOf<String>()
    for (str in arr) {
        var temp = ""
        for (i in str.indices) {
            if (vowels.contains(str[i].toString().toLowerCase()) || (i > 0 && str[i-1].toString().toLowerCase() == "y" && i < str.length - 1 && !vowels.contains(str[i].toString().toLowerCase()))) {
                temp += str[i]
            }
        }
        result.add(temp)
    }
    return result
}

fun separateNonVowels(arr: Array<String>): List<String> {
    val vowels = setOf('a', 'e', 'i', 'o', 'u')
    val result = mutableListOf<String>()
    for (str in arr) {
        var temp = ""
        for (i in str.indices) {
            val c = str[i].toLowerCase()
            if (!vowels.contains(c) || (i > 0 && str[i-1].toString().toLowerCase() == "y" && i < str.length - 1 && vowels.contains(str[i + 1].toLowerCase()))) {
                temp += c
            }
        }
        result.add(temp)
    }
    return result
}

fun calculatePythagoreanValue(string: String): Int {
    val words = upperCaseToLowerCase(string).split(" ")
    val arrayOfArrays = words.map { str ->
        str.map { letter ->
            pythagoreanValue(letter.toString())
        }.toIntArray()
    }.toTypedArray()

    val sums = arrayOfArrays.map { array ->
        array.sum()
    }.toIntArray()

    val result = sums.flatMap { num ->
        if (num >= 10) {
            val digits = num.toString().toCharArray().map { digit -> digit.toString().toInt() }
            listOf(digits.sum())
        } else {
            listOf(num)
        }
    }.toIntArray()

    var sum = 0
    for (num in result) {
        sum += num
    }

    while (sum !in setOf(11, 22, 33)) {
        if (sum >= 10) {
            sum = sum.toString().map { it.toString().toInt() }.sum()
        } else {
            break
        }
    }

    return sum
}

fun getFirstChar(arr: Array<String>): Int {
    val first = arr.map { pythagoreanValue(it[0].toString()) }.sum()
    var sum = 0
    if (first>=10){
        sum = separateAndSum(first)
    }else{
        return first
    }
    return sum
}

fun getLastString(arr: Array<String>): Int {
    val laststring = arr.lastOrNull().toString().map { pythagoreanValue(it.toString()) }
    var sum = 0
    if (laststring.toIntArray().sum()>=10){
        sum = separateAndSum(laststring.toIntArray().sum())
        return sum
    }else{
        return laststring.toIntArray().sum()
    }
}

fun getFirstString(arr: Array<String>): Int {
    val firststring = arr[0]
    var sum = 0
    if (firststring.toInt()>= 10){
        sum = separateAndSum(firststring.toInt())
        return sum
    }else{
        return firststring.toInt()
    }
}

fun countMissingNumbers(arr: IntArray): Int {
    val visited = BooleanArray(9)
    var count = 0
    if(arr.first()!=0){
        for (num in arr) {
            if (num in 1..9) {
                visited[num - 1] = true
            }
        }

        for (i in visited.indices) {
            if (!visited[i]) {
                count++
            }
        }
        return count
    }else{
        return 0
    }
}

fun getMostRepeatedNumbers(array: IntArray): List<Int> {
    val frequencyMap = mutableMapOf<Int, Int>()
    var maxFrequency = 0

    // count the frequency of each number in the array
    for (number in array) {
        val frequency = frequencyMap.getOrDefault(number, 0) + 1
        frequencyMap[number] = frequency
        if (frequency > maxFrequency) {
            maxFrequency = frequency
        }
    }

    // collect the numbers with the highest frequency
    val mostRepeatedNumbers = mutableListOf<Int>()
    for ((number, frequency) in frequencyMap) {
        if (frequency == maxFrequency) {
            mostRepeatedNumbers.add(number)
        }
    }

    return mostRepeatedNumbers
}

fun getDayOfBirth(str:String): Int {
    return separateAndSum(getDayMonth(str).first())
}

fun getMissingNumbers(array: IntArray): List<Int> {
    val countArray = IntArray(9)

    // Increment the value at the corresponding index in the new array
    for (i in array.indices) {
        val num = array[i]
        if (num in 1..9) {
            countArray[num - 1]++
        }
    }

    // Iterate through the new array and find any index with a value of zero
    val missingNumbers = mutableListOf<Int>()
    for (i in countArray.indices) {
        if (countArray[i] == 0) {
            missingNumbers.add(i + 1)
        }
    }

    return missingNumbers
}

fun getDayMonth(str: String): IntArray {
    val split = str.split("/")
    return split.take(2).map { it.toInt() }.toIntArray()
}

fun getDayYear(str: String): IntArray {
    val split = str.split("/")
    val first = split.first().toInt()
    val last = split.last().toInt()
    return intArrayOf(first,last)
}

fun getMonthYear(str: String): IntArray {
    val split = str.split("/")
    val seccond = split[1].toInt()
    val last = split.last().toInt()
    return intArrayOf(seccond,last)
}

fun reduceAndSum(array: IntArray): Int {
    var sum = array.sum()

    while (sum !in setOf(11, 22, 33)) {
        var digitSum = 0
        var num = sum

        while (num > 9) {
            digitSum += num % 10
            num /= 10
        }

        sum = digitSum
    }
    return sum
}

fun reduceYearToSingleDigit(): Int {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var reducedYear = currentYear
    var digitSum = 0
    var num = reducedYear

    while (reducedYear > 9) {
        digitSum += num % 10
        num /= 10

        reducedYear = digitSum
    }

    return reducedYear
}

fun getYear(str: String): Int {
    val array = str.split("/").map { it.toString().toInt() }.toIntArray()
    val lastElement = array.last()
    var reducedElement = lastElement

    while (reducedElement > 9) {
        var digitSum = 0
        var num = reducedElement

        while (num > 0) {
            digitSum += num % 10
            num /= 10
        }

        reducedElement = digitSum
    }

    return reducedElement
}

fun getNowMonth(): Int {
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    if (currentMonth >= 10) {
        return separateAndSum(currentMonth)
    } else {
        return currentMonth
    }
}

fun getMonth(str: String): Int {
    val array = str.split("/").map { it.toString().toInt() }.toIntArray()
    val seccondElement = array[1]
    var reducedElement = seccondElement

    while (reducedElement > 9) {
        var digitSum = 0
        var num = reducedElement

        while (num > 0) {
            digitSum += num % 10
            num /= 10
        }

        reducedElement = digitSum
    }

    return reducedElement
}

fun getNowDay(): Int{
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    return separateAndSum(currentDay)
}

fun abtractDayMonth(str: String): Int {
    val first = separateAndSum(getDayMonth(str).first())
    val seccond = separateAndSum(getDayMonth(str).last())
    return first - seccond

}

fun abtractDayYear(str: String): Int {
    val first = separateAndSum(getDayYear(str).first())
    val seccond = separateAndSum(getDayYear(str).last())
    return first - separateAndSum(seccond)
}

fun abtractMonthYear(str: String): Int {
    val first = getMonth(str)
    val seccond = getYear(str)
    if (seccond>=10){
        return first - separateAndSum(seccond)
    }
    return first - seccond
}

fun absoluteValue(num: Int): Int {
    return if (num < 0) -num else num
}