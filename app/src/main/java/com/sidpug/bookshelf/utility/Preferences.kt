package com.sidpug.bookshelf.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class Preferences {

    companion object {
        private const val NAME = "bookshelf.pref"
        lateinit var instance: SharedPreferences

        fun init(context: Context) {
            val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
            if (!Companion::instance.isInitialized) instance = EncryptedSharedPreferences.create(
                context,
                NAME,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }
}

fun SharedPreferences.setBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}

fun SharedPreferences.getBooleanData(key: String, defaultValue: Boolean): Boolean {
    return getBoolean(key, defaultValue)
}

private fun SharedPreferences.setStringData(key: String, value: String) {
    edit().putString(key, value).apply()
}

/**
 * Method to decrypt data, using CryptLib class
 * @param key: Key for the shared preference
 * @param dataToEncrypt: String value of data to encrypt
 */
@Throws(Exception::class)
fun SharedPreferences.encryptAndSaveData(key: String, dataToEncrypt: Any?) {
    try {
        setStringData(key, dataToEncrypt.toString())
    } catch (ex: Exception) {
        ex.showLog()
        throw ex
    }
}

fun SharedPreferences.getLongData(key: String, defaultValue: Long): Long {
    val decryptedData = decryptData(getString(key, ""))
    return decryptedData?.toLong() ?: defaultValue
}

fun SharedPreferences.getStringData(key: String, defaultValue: String): String {
    val decryptedData = decryptData(getString(key, defaultValue))
    return decryptedData ?: defaultValue
}

fun SharedPreferences.getIntData(key: String, defaultValue: Int): Int {
    val decryptedData = decryptData(getString(key, ""))
    return if (decryptedData.isNullOrEmpty()) defaultValue
    else decryptedData.toInt()
}

fun SharedPreferences.getFloatData(key: String, defaultValue: Float): Float {
    val decryptedData = decryptData(getString(key, ""))
    return decryptedData?.toFloat() ?: defaultValue
}

private fun decryptData(dataToDecrypt: String?): String? {
    try {
        if (!dataToDecrypt.isNullOrEmpty()) {
            return dataToDecrypt
        }
        return ""
    } catch (ex: Exception) {
        ex.showLog()
        return null
    }
}

fun SharedPreferences.clearPreferences() {
    this.edit().clear().apply()
}
