package org.wit.golfpoi.main

import android.app.Application
import org.wit.golfpoi.models.GolfPOIMemStore
import org.wit.golfpoi.models.GolfPOIModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    //val golfPOIs = ArrayList<GolfPOIModel>()
    val golfPOIs = GolfPOIMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Golf POI started")
        golfPOIs.create(GolfPOIModel("Wexford GOlf Course", "Beautiful par 70"))
        golfPOIs.create(GolfPOIModel("Cork GOlf Course", "Beautiful par 71"))
        golfPOIs.create(GolfPOIModel("Galway GOlf Course", "Beautiful par 72"))
    }
}