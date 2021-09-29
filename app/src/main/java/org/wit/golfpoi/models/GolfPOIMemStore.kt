package org.wit.golfpoi.models

import timber.log.Timber.i

// Managing golfPOI object id.
var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

class GolfPOIMemStore : GolfPOIStore {

    private val golfPOIs = ArrayList<GolfPOIModel>()

    override fun findAll(): List<GolfPOIModel> {
        return golfPOIs
    }

    // Add the passed in golfPOI object to the Arraylist in MemStore
    override fun create(golfPOI: GolfPOIModel) {
        golfPOI.id = getId()
        golfPOIs.add(golfPOI)
        logAll()
    }

    // Update the golfPOI object passed in as reference
    override fun update(golfPOI: GolfPOIModel) {
        var foundGolfPOI : GolfPOIModel? = golfPOIs.find { p -> p.id == golfPOI.id }
        if (foundGolfPOI != null) {
            foundGolfPOI.courseTitle = golfPOI.courseTitle
            foundGolfPOI.courseDescription = golfPOI.courseDescription
            logAll()
        }
    }

    fun logAll() {
        golfPOIs.forEach{(i("${it}"))}
    }
}