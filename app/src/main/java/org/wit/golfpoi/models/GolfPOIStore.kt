package org.wit.golfpoi.models

interface GolfPOIStore {

    fun findAllPOIs(): List<GolfPOIModel>
    fun createPOI(golPOI: GolfPOIModel)
    fun updatePOI(golfPOI: GolfPOIModel)
    fun removePOI(position: Int)

}