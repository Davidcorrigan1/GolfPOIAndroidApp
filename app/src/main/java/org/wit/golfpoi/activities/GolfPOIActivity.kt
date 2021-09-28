package org.wit.golfpoi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.golfpoi.databinding.ActivityGolfpoiBinding
import org.wit.golfpoi.main.MainApp
import org.wit.golfpoi.models.GolfPOIModel
import timber.log.Timber
import timber.log.Timber.i

class GolfPOIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGolfpoiBinding
    var golfPOI = GolfPOIModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGolfpoiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        app = application as MainApp

        i("GOLF POI Activity started..")

        binding.btnAdd.setOnClickListener() {
            golfPOI.courseTitle = binding.golfPOITitle.text.toString()
            golfPOI.courseDescription = binding.golfPOIDesc.text.toString()
            if (golfPOI.courseTitle.isNotEmpty() && golfPOI.courseDescription.isNotEmpty()) {
                i("add Button Pressed ${golfPOI.courseTitle} and ${golfPOI.courseDescription}")
                app.golfPOIs.add(golfPOI.copy())

                for (i in app.golfPOIs.indices) {
                    i("Golf Course [$i] : ${app.golfPOIs[i]}")
                }
            } else {
                Snackbar
                    .make(it, "Please enter a Title and Desc", Snackbar.LENGTH_LONG)
                    .show()
            }

        }
    }
}