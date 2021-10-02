package org.wit.golfpoi.models

import timber.log.Timber.i
import java.text.FieldPosition

// Managing golfPOI object id.
var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

class GolfPOIMemStore : GolfPOIStore {

    private val golfPOIs = ArrayList<GolfPOIModel>()

    override fun findAllPOIs(): List<GolfPOIModel> {
        return golfPOIs
    }

    // Add the passed in golfPOI object to the Arraylist in MemStore
    override fun createPOI(golfPOI: GolfPOIModel) {
        golfPOI.id = getId()
        golfPOIs.add(golfPOI)
        logAll()
    }

    // Update the golfPOI object passed in as reference
    override fun updatePOI(golfPOI: GolfPOIModel) {
        var foundGolfPOI : GolfPOIModel? = golfPOIs.find { p -> p.id == golfPOI.id }
        if (foundGolfPOI != null) {
            foundGolfPOI.courseTitle = golfPOI.courseTitle
            foundGolfPOI.courseDescription = golfPOI.courseDescription
            foundGolfPOI.courseProvince = golfPOI.courseProvince
            logAll()
        }
    }

    // Remove the item at position passed in
    override fun removePOI(position: Int) {

        if (golfPOIs[position] != null) {
            golfPOIs.removeAt(position)
        }
    }

    fun logAll() {
        golfPOIs.forEach{(i("${it}"))}
    }
}