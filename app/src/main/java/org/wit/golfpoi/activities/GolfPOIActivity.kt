package org.wit.golfpoi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
import org.wit.golfpoi.R
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
        var editFlag = false
        var setProvinces : String = ""

        binding = ActivityGolfpoiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind the toolbar and set the title
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        Timber.plant(Timber.DebugTree())
        app = application as MainApp

        // creating objects needed for the spinner drop down
        // Dropdown of Provinces taken from the strings resource file
        val provinces = resources.getStringArray(R.array.provinces)
        val spinner : Spinner = findViewById(R.id.provinceSpinner)

        // Create an ArrayAdapter object with the dropdown type
        // and populate using the list of the provinces.

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, provinces)
        spinner.adapter = adapter

        i("GOLF POI Activity started..")

        // Are we coming from the List Activity with Data being passed via Parcelize...
        if (intent.hasExtra("golfpoi_edit")) {
            golfPOI = intent.extras?.getParcelable("golfpoi_edit")!!
            binding.golfPOITitle.setText(golfPOI.courseTitle)
            binding.golfPOIDesc.setText(golfPOI.courseDescription)
            binding.btnAdd.setText(R.string.button_saveGolfPOI)

            // check the current selected provence and default to that one!
            var spinnerPosition : Int = adapter.getPosition(golfPOI.courseProvince)
            spinner.setSelection(spinnerPosition)
            i("Setting the dropdown default from model value")

            editFlag = true
        }

        // Listener for the spinner dropdown for provinces
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                i("Selected: ${getString(R.string.selected_item)} ${provinces[p2]}" )
                setProvinces = provinces[p2]
                i("The selected provence is: $setProvinces")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                i("nothing selected")
            }
        }

        // Listener and action for the button
        binding.btnAdd.setOnClickListener() {
            golfPOI.courseTitle = binding.golfPOITitle.text.toString()
            golfPOI.courseDescription = binding.golfPOIDesc.text.toString()
            golfPOI.courseProvince = setProvinces.toString()
            i("Setting the model province to $setProvinces")
            if (golfPOI.courseTitle.isNotEmpty() && golfPOI.courseDescription.isNotEmpty()) {
                if (editFlag) {
                    i("save Button Pressed ${golfPOI.courseTitle} and ${golfPOI.courseDescription}")
                    app.golfPOIs.updatePOI(golfPOI.copy())
                } else {
                    i("add Button Pressed ${golfPOI.courseTitle} and ${golfPOI.courseDescription}")
                    app.golfPOIs.createPOI(golfPOI.copy())
                }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar
                    .make(it, "Please enter a Title and Desc", Snackbar.LENGTH_LONG)
                    .show()
            }

        }
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_golfpoi, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Handle the clicking of the menu cancel item. Return to previous activity.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}