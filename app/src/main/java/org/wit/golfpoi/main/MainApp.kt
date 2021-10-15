package org.wit.golfpoi.main

import android.app.Application
import android.net.Uri
import org.wit.golfpoi.models.GolfPOIMemStore
import org.wit.golfpoi.models.GolfPOIModel
import org.wit.golfpoi.models.GolfUserModel
import timber.log.Timber
import timber.log.Timber.i
import java.time.LocalDate

class MainApp : Application() {

    //val golfPOIs = ArrayList<GolfPOIModel>()
    val golfPOIs = GolfPOIMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Golf POI started")
        golfPOIs.createPOI(GolfPOIModel(1000,"Wexford GOlf Course", "Beautiful par 72 in the west of this beautiful county. The greens on this course are like carpet and the fairways are immalculate.", "Munster", 72, Uri.EMPTY, 52.25260421972287, -7.338150110840797, 15f))
        golfPOIs.createPOI(GolfPOIModel(1001,"Cork GOlf Course", "Beautiful par 72 in the west of this beautiful county. The greens on this course are like carpet and the fairways are immalculate.", "Leinster", 71, Uri.EMPTY, 52.25260421972287, -7.158150110840797, 15f))
        golfPOIs.createPOI(GolfPOIModel(1002,"Galway GOlf Course", "Beautiful par 72 in the west of this beautiful county. The greens on this course are like carpet and the fairways are immalculate.", "Leinster", 70, Uri.EMPTY, 52.25260421972287, -7.138150110840797, 15f))
        golfPOIs.createPOI(GolfPOIModel(1003,"Donegal GOlf Course", "Beautiful par 72 in the west of this beautiful county. The greens on this course are like carpet and the fairways are immalculate. I would totally recommend this course to anyone wishing to play", "Leinster", 72, Uri.EMPTY, 52.23260421972287, -7.138150110840797, 15f))
        golfPOIs.createPOI(GolfPOIModel(1004,"Cork GOlf Course", "Beautiful par 72 in the west of this beautiful county. The greens on this course are like carpet and the fairways are immalculate. I would totally recommend this course to anyone wishing to play", "Leinster", 71, Uri.EMPTY, 52.24260421972287, -7.138150110840797, 15f))
        golfPOIs.createPOI(GolfPOIModel(1005,"Roscommon GOlf Course", "Beautiful par 72 in the west of this beautiful county. The greens on this course are like carpet and the fairways are immalculate. I would totally recommend this course to anyone wishing to play", "Leinster", 70, Uri.EMPTY, 52.25260421972287, -7.138150110840797, 15f))
        golfPOIs.createPOI(GolfPOIModel(1006,"Dublin GOlf Course", "Beautiful par 72 in the west of this beautiful county. The greens on this course are like carpet and the fairways are immalculate. I would totally recommend this course to anyone wishing to play", "Leinster", 71, Uri.EMPTY, 52.45260421972287, -7.138150110840797, 15f))
        golfPOIs.createPOI(GolfPOIModel(1007,"Wicklow GOlf Course", "Beautiful par 72 in the west of this beautiful county. The greens on this course are like carpet and the fairways are immalculate. I would totally recommend this course to anyone wishing to play", "Leinster", 70, Uri.EMPTY, 52.75260421972287, -7.138150110840797, 15f))

        golfPOIs.createUser(GolfUserModel(2000, "davidcorrigan1@gmail.com","Pass1976", "David", "Corrigan", LocalDate.now(), 1 ))
    }
}