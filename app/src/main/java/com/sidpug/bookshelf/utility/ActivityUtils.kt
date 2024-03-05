package com.sidpug.bookshelf.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(
    fragment: Fragment, @IdRes frameId: Int,
    addBackStack: String? = null
) {
    supportFragmentManager.beginTransaction().apply {
        if (addBackStack != null) {
            addToBackStack(addBackStack)
        }
        replace(frameId, fragment, addBackStack)
        commit()
    }
}

fun AppCompatActivity.removeFragmentFromContainer(@IdRes containerId: Int) {
    supportFragmentManager.findFragmentById(containerId)?.let {
        supportFragmentManager.beginTransaction().remove(it).commit()
    }
}

fun Fragment.removeFragmentFromContainer(@IdRes containerId: Int) {
    childFragmentManager.findFragmentById(containerId)?.let {
        childFragmentManager.beginTransaction().remove(it).commit()
    }
}

fun Fragment.replaceChildFragment(
    childFragmentToOpen: Fragment,
    @IdRes frameId: Int,
    addBackStack: String? = null
) {
    val fragTransaction = childFragmentManager.beginTransaction()
    fragTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
    fragTransaction.apply {
        if (addBackStack != null) {
            addToBackStack(addBackStack)
        }
        replace(frameId, childFragmentToOpen)
        commitAllowingStateLoss()
    }
}

/**
 * Kotlin Extensions for simpler, easier and fun way
 * of launching of Activities
 */

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> Context.launchActivity(
    activity: FragmentActivity,
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    val activityLauncher = CustomActivityResult.registerActivityForResult(activity)
    activityLauncher.launch(intent)
    //activity.startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

// EXAMPLES FOR THE ABOVE FUNCTIONS

// Simple Activities
//launchActivity<UserDetailActivity>()

// Add Intent extras
/*launchActivity<UserDetailActivity> {
    putExtra(INTENT_USER_ID, user.id)
}*/

// Add custom flags
/*launchActivity<UserDetailActivity> {
    putExtra(INTENT_USER_ID, user.id)
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
}*/

// Add Shared Transistions
/*val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, avatar, "avatar")
launchActivity<UserDetailActivity>(options = options) {
    putExtra(INTENT_USER_ID, user.id)
}*/

// Add requestCode for startActivityForResult() call
/*
launchActivity<UserDetailActivity>(requestCode = 1234) {
    putExtra(INTENT_USER_ID, user.id)
}*/

