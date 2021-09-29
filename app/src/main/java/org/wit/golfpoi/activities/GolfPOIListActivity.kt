package org.wit.golfpoi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.golfpoi.R
import org.wit.golfpoi.adapter.GolfPOIAdapter
import org.wit.golfpoi.databinding.ActivityGolfPoilistBinding
import org.wit.golfpoi.main.MainApp

class GolfPOIListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGolfPoilistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGolfPoilistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GolfPOIAdapter(app.golfPOIs)
    }
}