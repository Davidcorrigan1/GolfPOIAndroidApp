package org.wit.golfpoi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        Timber.plant(Timber.DebugTree())

        // Set the recyclerView layout and link the adapter
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GolfPOIAdapter(app.golfPOIs.findAllPOIs(), this)

        setRecyclerViewItemTouchListener()
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

    override fun onGolfPOIClick(golfPOI: GolfPOIModel) {
        val launcherIntent = Intent(this, GolfPOIActivity::class.java)
        launcherIntent.putExtra("golfpoi_edit", golfPOI)
        startActivityForResult(launcherIntent,0)
    }

    // Instruct the adapter that the Data model has updated data.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
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
                app.golfPOIs.removePOI(position)
                //photosList.removeAt(position)
                binding.recyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        //4
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

}