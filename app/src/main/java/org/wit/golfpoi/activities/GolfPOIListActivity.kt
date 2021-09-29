package org.wit.golfpoi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        // Inflate the layout and Bind it to the activity
        binding = ActivityGolfPoilistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable action bar and give it a title
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        // Initialise the app object
        app = application as MainApp

        // Set the recyclerView layout and link the adapter
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GolfPOIAdapter(app.golfPOIs)


    }

    // Overload method to load the menu resource
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Implements a menu event handler; if the event is item_add then the GolfPOIActivity is started
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GolfPOIActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}