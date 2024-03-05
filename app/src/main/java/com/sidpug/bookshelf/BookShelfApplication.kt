package com.sidpug.bookshelf

import android.app.Activity
import android.app.Application
import android.os.Bundle
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.sidpug.bookshelf.utility.Preferences
import com.sidpug.bookshelf.utility.showLog

class BookShelfApplication : Application(), Application.ActivityLifecycleCallbacks {

    private var activityReferenceCount: Int = 0
    private var isActivityConfigurationChanged: Boolean = false

    companion object {
        lateinit var instance: BookShelfApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
//            throwable.showLog()
//        }
        instance = this
        Preferences.init(applicationContext)
        registerActivityLifecycleCallbacks(this)
        initCoilImageLoader()
    }

    /**
     * Reference: https://coil-kt.github.io/coil/image_loaders/
     */
    private fun initCoilImageLoader() {
        ImageLoader.Builder(applicationContext).memoryCache {
            //Set the maximum size of the memory cache as a percentage of this application's available memory.
            MemoryCache.Builder(applicationContext).maxSizePercent(0.25).build()
        }.diskCache {
            DiskCache.Builder().directory(applicationContext.cacheDir.resolve("image_cache"))
                //Set the maximum size of the disk cache as a percentage of the device's free disk space.
                .maxSizePercent(0.5).build()
        }.build()
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {
        // App enters foreground
        if (++activityReferenceCount == 1 && !isActivityConfigurationChanged) {
            showLog("APP IN FOREGROUND")
        }
    }

    override fun onActivityResumed(p0: Activity) {

    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        isActivityConfigurationChanged = activity.isChangingConfigurations
        if (--activityReferenceCount == 0 && !isActivityConfigurationChanged) {
            showLog("APP IN BACKGROUND")
        }
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {

    }
}