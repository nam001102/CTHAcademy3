import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import android.util.Base64
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
@SuppressLint("StaticFieldLeak")
private val db = FirebaseFirestore.getInstance()

fun decimalToAlphabet(str : String) : String{
    val digitArray = str.map { it.toString().toInt() }.toTypedArray()
    val shiftTimes = digitArray.last()
    val digitToCharMap = ('A'..'Z').toList().withIndex().associate { (index, char) -> index to char }
    val charArray = digitArray.map { digitToCharMap[it] ?: '?' }
    val result = charArray.joinToString("")

    return shiftStringForward(result, shiftTimes)
}
fun alphabetToDecimal(str : String,phone: String) : String{
    val digitArray = phone.map { it.toString().toInt() }.toTypedArray()
    val shiftTimes = digitArray.last()
    val charArray = shiftStringBackward(str, shiftTimes).toCharArray()
    val numberArray = mutableListOf<String>()
    for (char in charArray){
        numberArray.add(convertToNumeric(char))
    }

    return numberArray.toString()
}
private fun convertToNumeric(inputText: Char): String {
    val uppercaseInput = inputText.toUpperCase()
    var numericValue = 0


    if (uppercaseInput.isLetter()) {
        val numericCharValue = uppercaseInput.toInt() - 'A'.toInt()
        numericValue = numericValue * 26 + numericCharValue
    } else {
        // Handle non-letter characters here if needed
    }


    return numericValue.toString()
}
private fun shiftStringForward(input: String, shiftAmount: Int): String {
    val shiftedChars = input.map { char ->
        if (char.isLetter()) {
            val base = if (char.isUpperCase()) 'A' else 'a'
            val shiftedChar = (base + (char - base + shiftAmount) % 26).toChar()
            shiftedChar
        } else {
            char // Keep non-letter characters unchanged
        }
    }
    return shiftedChars.joinToString("")
}
private fun shiftStringBackward(input: String, shiftAmount: Int): String {
    val shiftedText = StringBuilder()

    for (char in input) {
        if (char.isLetter()) {
            val isUpperCase = char.isUpperCase()
            val shiftedChar = if (isUpperCase) {
                ((char - 'A' + 26 - shiftAmount) % 26 + 'A'.toInt()).toChar()
            } else {
                ((char - 'a' + 26 - shiftAmount) % 26 + 'a'.toInt()).toChar()
            }
            shiftedText.append(shiftedChar)
        } else {
            shiftedText.append(char)
        }
    }
    return shiftedText.toString()
}

fun hashAndSavePasswordHash(context: Context, clearPassword: String) {
    val digest = MessageDigest.getInstance("SHA-1")
    val result = digest.digest(clearPassword.toByteArray(Charsets.UTF_8))
    val sb = StringBuilder()
    for (b in result) {
        sb.append(String.format("%02X", b))
    }
    val hashedPassword = sb.toString()
//    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
//    val editor = sharedPref.edit()
//    editor.putString("password_hash", hashedPassword)
//    editor.apply()
}

fun getSavedPasswordHash(context: Context): String {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    return if (sharedPref.contains("password_hash"))
        sharedPref.getString("password_hash", "")!!
    else
        ""
}

fun getHashForString(context: Context, stringToHash: String): String {
    val digest = MessageDigest.getInstance("SHA-1")
    val result = digest.digest(stringToHash.toByteArray(Charsets.UTF_8))
    val sb = StringBuilder()
    for (b in result) {
        sb.append(String.format("%02X", b))
    }
    return sb.toString()
}

fun encryptAndSavePassword(strToEncrypt: String, phone : String): String {
    val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)
    val keygen = KeyGenerator.getInstance("AES")
    keygen.init(256)
    val key = keygen.generateKey()
    val saveKey =  saveSecretKey(key)
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val cipherText = cipher.doFinal(plainText)
    val cipherkey = saveInitializationVector(cipher.iv)

    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(cipherText)
    val strToSave = String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
//    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
//    val editor = sharedPref.edit()
//    editor.putString("encrypted_password", strToSave)
//    editor.apply()

    return "{${cipherkey}},{${saveKey}},{${cipherText}},{${strToSave}}"
}

fun getDecryptedPassword(encrypted: String, key: String,vector: String): String {
    val bytes = Base64.decode(encrypted, Base64.DEFAULT)
    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
    val passwordToDecryptByteArray = ois.readObject() as ByteArray
    val decryptedPasswordByteArray = decrypt(passwordToDecryptByteArray, key,vector)

    val decryptedPassword = StringBuilder()
    for (b in decryptedPasswordByteArray) {
        decryptedPassword.append(b.toChar())
    }

    return decryptedPassword.toString()
}

private fun decrypt(dataToDecrypt: ByteArray, key: String,vector: String): String {
    val secretKey = getSavedSecretKey(key)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val ivSpec = IvParameterSpec(getSavedInitializationVector(vector))
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
    val decryptedData = cipher.doFinal(dataToDecrypt)
    return decryptedData.toString(Charsets.UTF_8)

}


private fun saveSecretKey(secretKey: SecretKey): String {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(secretKey)
    return String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))

}

private fun getSavedSecretKey(key: String): SecretKey {

    val bytes = Base64.decode(key, Base64.DEFAULT)
    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
    return ois.readObject() as SecretKey

}

private fun saveInitializationVector(initializationVector: ByteArray): String {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(initializationVector)
    return String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
}

private fun getSavedInitializationVector(vector: String): ByteArray {
    val bytes = Base64.decode(vector, Base64.DEFAULT)
    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
    return ois.readObject() as ByteArray
}
