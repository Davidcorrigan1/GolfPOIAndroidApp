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
        golfPOIs.add(GolfPOIModel("Wexford GOlf Course", "Beautiful par 70"))
        golfPOIs.add(GolfPOIModel("Cork GOlf Course", "Beautiful par 71"))
        golfPOIs.add(GolfPOIModel("Galway GOlf Course", "Beautiful par 72"))
    }
}