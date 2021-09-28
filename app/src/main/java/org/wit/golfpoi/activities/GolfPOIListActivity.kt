package org.wit.golfpoi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.wit.golfpoi.R
import org.wit.golfpoi.main.MainApp

class GolfPOIListActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_golf_poilist)

        app = application as MainApp
    }
}