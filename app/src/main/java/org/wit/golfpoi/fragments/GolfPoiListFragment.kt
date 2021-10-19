package org.wit.golfpoi.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.golfpoi.adapter.GolfPOIAdapter
import org.wit.golfpoi.adapter.GolfPOIListener
import org.wit.golfpoi.databinding.FragmentGolfPoiListBinding
import org.wit.golfpoi.main.MainApp
import org.wit.golfpoi.models.GolfPOIModel
import org.wit.golfpoi.models.Location
import timber.log.Timber.i


class GolfPoiListFragment : Fragment(), GolfPOIListener {
    lateinit var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private var _fragBinding: FragmentGolfPoiListBinding? = null
    private val fragBinding get() = _fragBinding!!

    // When the Fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    // When the view is created
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {
        _fragBinding = FragmentGolfPoiListBinding.inflate(inflater, container, false)
        val root = fragBinding?.root

        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        fragBinding.recyclerView.adapter = GolfPOIAdapter(app.golfPOIData.findAllPOIs(),this)

        setRecyclerViewItemTouchListener(fragBinding)
        registerRefreshCallback(fragBinding)

        return root
    }

    override fun onResume() {
        super.onResume()
        i("fragment resuming")
        i("${app.golfPOIData.findAllPOIs()}")
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GolfPoiListFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    // Handle the click of the Add button to trigger navigation and send the data
    override fun onGolfPOIClick(golfPOI: GolfPOIModel) {
        val action = GolfPoiListFragmentDirections.actionGolfPoiListFragmentToGolfPoiFragment(golfPOI)
        findNavController().navigate(action)
    }

    // Method to handle deleting an item with a swipe
    private fun setRecyclerViewItemTouchListener(layout:  FragmentGolfPoiListBinding) {

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
                layout.recyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        //4
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(layout.recyclerView)
    }

    // Register the Callback Function
    private fun registerRefreshCallback(layout: FragmentGolfPoiListBinding) {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { layout.recyclerView.adapter?.notifyDataSetChanged() }
    }
}