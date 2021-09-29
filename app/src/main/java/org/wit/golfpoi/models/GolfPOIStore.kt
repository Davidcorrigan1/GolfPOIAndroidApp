package org.wit.golfpoi.models

interface GolfPOIStore {

    fun findAll(): List<GolfPOIModel>
    fun create(golPOI: GolfPOIModel)

}