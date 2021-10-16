package org.wit.golfpoi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.golfpoi.R
import org.wit.golfpoi.adapter.GolfPOIAdapter
import org.wit.golfpoi.adapter.GolfPOIListener
import org.wit.golfpoi.databinding.ActivityGolfPoilistBinding
import org.wit.golfpoi.main.MainApp
import org.wit.golfpoi.models.GolfPOIModel
import timber.log.Timber
import timber.log.Timber.i

class GolfPOIListActivity : AppCompatActivity(), GolfPOIListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGolfPoilistBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise the app object
        app = application as MainApp

        // Inflate the layout and Bind it to the activity
        binding = ActivityGolfPoilistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable action bar and give it a title
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        // Set the recyclerView layout and link the adapter
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GolfPOIAdapter(app.golfPOIData.findAllPOIs(), this)

        // Plant timber for logging
        Timber.plant(Timber.DebugTree())

        registerRefreshCallback()

        setRecyclerViewItemTouchListener()
    }

    // Overload method to load the menu resource
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_golfpoilist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Implements a menu event handler; if the event is item_add then the GolfPOIActivity is started
    // Implements a logout button which will return to the login screen acticity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GolfPOIActivity::class.java)
                //startActivityForResult(launcherIntent,0)
                refreshIntentLauncher.launch(launcherIntent)
            }

            R.id.item_logout -> {
                val launcherIntent = Intent(this, GolfPOILoginActivity::class.java)
                startActivityForResult(launcherIntent,0)
                refreshIntentLauncher.launch(launcherIntent)
            }

            R.id.item_map -> {
                val launcherIntent = Intent(this, GolfPOIOverviewMapActivity::class.java)
                var mapLocations = arrayListOf<GolfPOIModel>()
                for (golfPOI in app.golfPOIData.findAllPOIs()) {
                    mapLocations.add(golfPOI)
                }
                launcherIntent.putParcelableArrayListExtra("MapLocations", mapLocations)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // The onGolfPOIClick listener.
    override fun onGolfPOIClick(golfPOI: GolfPOIModel) {
        val launcherIntent = Intent(this, GolfPOIActivity::class.java)
        launcherIntent.putExtra("golfpoi_edit", golfPOI)
        refreshIntentLauncher.launch(launcherIntent)
    }

    // Method to handle deleting an item with a swipe
    private fun setRecyclerViewItemTouchListener() {

        // Create the callback and tell it what events to listen for
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {
                // Return false in onMove. You don’t want to perform any special behavior here
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // Call onSwiped when you swipe an item in the direction specified in the ItemTouchHelper.
                // Here, you request the viewHolder parameter passed for the position of the item view,
                // and then you remove that item from your list of photos.
                // Finally, you inform the RecyclerView adapter that an item has been removed at a specific position
                val position = viewHolder.adapterPosition
                i("Deleting Item At position $position")
                app.golfPOIData.removePOI(position)
                //photosList.removeAt(position)
                binding.recyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        //4
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    // Register the Callback Function
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { binding.recyclerView.adapter?.notifyDataSetChanged() }
    }

}