package org.wit.golfpoi.main

import android.app.Application
import org.wit.golfpoi.models.GolfPOIModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val golfPOIs = ArrayList<GolfPOIModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Golf POI started")
    }
}