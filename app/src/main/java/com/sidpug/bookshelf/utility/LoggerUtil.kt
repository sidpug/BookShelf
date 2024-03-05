package com.sidpug.bookshelf.utility

import android.os.Bundle
import android.util.Log
import com.sidpug.bookshelf.BuildConfig

const val DEFAULT_TAG = "BOOKSHELF - "

enum class LogLevel {
    NONE,
    INFO,
    VERBOSE,
    DEBUG,
    ERROR
}

fun showLog(message: String?, logsLevel: LogLevel = LogLevel.NONE) {
    if (BuildConfig.DEBUG) {
        Log.e(DEFAULT_TAG, if (message.isNullOrEmpty()) "Empty message" else message)
    }
}

fun showLog(tag: String = DEFAULT_TAG, message: String?) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, if (message.isNullOrEmpty()) "Empty message" else message)
    }
}

fun Exception.showLog() {
    if (BuildConfig.DEBUG) {
        printFullLog(this.stackTraceToString())
    } else reportToCrashlytics(this)
}

private fun printFullLog(message: String) {
    if (message.length > 3000) {
        Log.e(DEFAULT_TAG, message.substring(0, 3000))
        printFullLog(message.substring(3000))
    } else {
        Log.e(DEFAULT_TAG, message)
    }
}

fun Throwable.showLog() {
    if (BuildConfig.DEBUG) this.printStackTrace()
    else reportToCrashlytics(Exception(this))
}

fun OutOfMemoryError.showLog() {
    if (BuildConfig.DEBUG) this.showLog()
    else reportToCrashlytics(Exception("OutOfMemoryError"))
}

private fun reportToCrashlytics(e: Exception) {
    // FirebaseCrashlytics.getInstance().recordException(this)
    //e.showLog()
}

/**
 * Log contents of bundle in debug mode
 */
fun showLog(bundle: Bundle?, tag: String = DEFAULT_TAG) {
    if (BuildConfig.DEBUG) if (bundle != null) {
        for (key in bundle.keySet()) {
            Log.e(tag, "$key : ${bundle.get(key).toString()}")
        }
    }
}

/**
 * Iterate through hash map
 */
fun showLog(hashMap: HashMap<Int, ArrayList<String>>?, tag: String = DEFAULT_TAG) {
    if (BuildConfig.DEBUG) if (hashMap != null) {
        for ((k, v) in hashMap) {
            Log.e(tag, "$k = $v")
        }
    } else showLog("Hashmap is null", DEFAULT_TAG)
}
