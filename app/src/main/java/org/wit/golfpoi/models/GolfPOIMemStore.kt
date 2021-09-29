package org.wit.golfpoi.models

import timber.log.Timber.i

class GolfPOIMemStore : GolfPOIStore {

    private val golfPOIs = ArrayList<GolfPOIModel>()

    override fun findAll(): List<GolfPOIModel> {
        return golfPOIs
    }

    override fun create(golfPOI: GolfPOIModel) {
        golfPOIs.add(golfPOI)
    }

    fun logAll() {
        golfPOIs.forEach{(i("${it}"))}
    }
}