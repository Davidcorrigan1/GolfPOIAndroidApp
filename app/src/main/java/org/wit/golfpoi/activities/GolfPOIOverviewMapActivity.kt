package org.wit.golfpoi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.golfpoi.R
import org.wit.golfpoi.databinding.ActivityGolfPoioverviewMapBinding
import org.wit.golfpoi.models.GolfPOIModel
import timber.log.Timber.i

var golfPOIs = arrayListOf<GolfPOIModel>()
class GolfPOIOverviewMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityGolfPoioverviewMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGolfPoioverviewMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Gets an arrayList of course details from the GolfCourseList activity
        if (intent.hasExtra("MapLocations")) {
            i("Getting Map Locations")
            golfPOIs = intent.extras?.getParcelableArrayList("MapLocations")!!
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * This is where we can add markers for all our courses
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // create a BoundsBuilder object to help frame the map
        // For each golfCourse in the Arraylist from last Activity
        // if the location is populated create Latlng object
        // Include each map location in the boundsBuilder
        // Create a marker for each golf course with title and desc included.
                val boundsBuilder = LatLngBounds.builder()
        for (golfPOI in golfPOIs) {
            if (golfPOI.lat != 0.0 && golfPOI.lng != 0.0) {
                val latlng = LatLng(golfPOI.lat, golfPOI.lng)
                boundsBuilder.include(latlng)
                map.addMarker(
                    MarkerOptions().position(latlng).title(golfPOI.courseTitle)
                        .snippet(golfPOI.courseDescription)
                )

            }
        }
        // Move the'camera' for that it zooms to show all the Golf Courses
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),1000,1000,0))
    }
}