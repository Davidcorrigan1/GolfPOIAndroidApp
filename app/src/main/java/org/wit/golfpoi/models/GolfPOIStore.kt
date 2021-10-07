package org.wit.golfpoi.models

interface GolfPOIStore {

    fun findAllPOIs(): List<GolfPOIModel>
    fun createPOI(golPOI: GolfPOIModel)
    fun updatePOI(golfPOI: GolfPOIModel)
    fun removePOI(position: Int)

    fun createUser(user: GolfUserModel)
    fun findUser(email: String, password: String): GolfUserModel?

}