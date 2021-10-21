package org.wit.golfpoi.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.golfpoi.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE_POIS = "golfPOIs.json"
const val JSON_FILE_USER = "golfUsers.json"

val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listTypePOIS: Type = object : TypeToken<ArrayList<GolfPOIModel>>() {}.type
val listTypeUSER: Type = object : TypeToken<ArrayList<GolfUserModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GolfPOIJSONStore(private val context: Context) : GolfPOIStore {

    private var golfPOIs = mutableListOf<GolfPOIModel>()
    private var users = mutableListOf<GolfUserModel>()
    private var currentUser : GolfUserModel = GolfUserModel()

    // Deserialize the POI and User files into the Data objects
    init {
        if (exists(context, JSON_FILE_POIS)) {
            deserialize(JSON_FILE_POIS, listTypePOIS)
        }
        if (exists(context, JSON_FILE_USER)) {
            deserialize(JSON_FILE_USER, listTypeUSER)
        }
    }

    // Return all the existing Golf Courses
    override fun findAllPOIs(): MutableList<GolfPOIModel> {
        return golfPOIs
    }

    // Create a new Golf Course in the List and update the JSON file
    override fun createPOI(golfPOI: GolfPOIModel) {
        golfPOI.id = generateRandomId()
        golfPOIs.add(golfPOI)
        serialize(JSON_FILE_POIS, listTypePOIS)
    }


    // Update the golfPOI object passed in as reference and update JSON file
    override fun updatePOI(golfPOI: GolfPOIModel) {
        Timber.i("In updatePOI: $golfPOI")
        val foundGolfPOI : GolfPOIModel? = golfPOIs.find { p -> p.id == golfPOI.id }
        if (foundGolfPOI != null) {
            Timber.i("in updatePOI after finding POI: $foundGolfPOI")
            foundGolfPOI.courseTitle = golfPOI.courseTitle
            foundGolfPOI.courseDescription = golfPOI.courseDescription
            foundGolfPOI.courseProvince = golfPOI.courseProvince
            foundGolfPOI.coursePar = golfPOI.coursePar
            foundGolfPOI.image = golfPOI.image
            foundGolfPOI.lat = golfPOI.lat
            foundGolfPOI.lng = golfPOI.lng
            foundGolfPOI.zoom = golfPOI.zoom
            serialize(JSON_FILE_POIS, listTypePOIS)
            logAll()
        }
    }

    // Remove the item at position passed in, and update the JSON file
    override fun removePOI(position: Int) {
        if (golfPOIs[position] != null) {
            golfPOIs.removeAt(position)
            serialize(JSON_FILE_POIS, listTypePOIS)
        }
    }

    // Find a Golf Course POI based on id
    override fun findPOI(id: Long): GolfPOIModel? {
        return golfPOIs.find { p -> p.id == id }
    }

    // Generate a user id and add user passed in to the array and update JSON File
    override fun createUser(user: GolfUserModel) {
        user.id = generateRandomId()
        users.add(user)
        serialize(JSON_FILE_USER, listTypeUSER)
        logAllUsers()
    }

    // Use the email address to find a user if it exists, if found check the
    // password matches the supplied. If match return the user object else null.
    override fun findUser(email: String): GolfUserModel? {
        Timber.i("user entered Email: $email")
        var userFound: GolfUserModel? = users.find{ it.userEmail == email }
        Timber.i("userFound: $userFound")
        if (userFound != null) {
            return userFound
        } else {
            userFound = null
        }
        return userFound
    }

    // Set the passed in user to be the current User
    override fun setCurrentUser(user: GolfUserModel) {
        currentUser = user
    }

    override fun getCurrentUser(): GolfUserModel {
        return currentUser
    }


    private fun serialize(fileName: String, listType: Type) {
        val jsonString: String
        if (fileName == JSON_FILE_POIS) {
            jsonString = gsonBuilder.toJson(golfPOIs, listType)
        } else {
            jsonString = gsonBuilder.toJson(users, listType)
        }
        write(context, fileName, jsonString)
    }

    private fun deserialize(fileName: String, listType: Type) {
        val jsonString = read(context, fileName)
        if (fileName == JSON_FILE_POIS) {
            golfPOIs = gsonBuilder.fromJson(jsonString, listType)
        } else {
            users = gsonBuilder.fromJson(jsonString, listType)
        }

    }

    private fun logAll() {
        golfPOIs.forEach { Timber.i("$it") }
    }

    private fun logAllUsers() {
        users.forEach{ Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}