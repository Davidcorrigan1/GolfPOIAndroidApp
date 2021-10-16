package org.wit.golfpoi.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.golfpoi.R
import org.wit.golfpoi.databinding.ActivityGolfpoiBinding
import org.wit.golfpoi.helpers.showImagePicker
import org.wit.golfpoi.main.MainApp
import org.wit.golfpoi.models.GolfPOIModel
import org.wit.golfpoi.models.Location
import timber.log.Timber
import timber.log.Timber.i

class GolfPOIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGolfpoiBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    var golfPOI = GolfPOIModel()
    lateinit var app : MainApp
    var location = Location("Current", 52.245696, -7.139102, 15f)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var editFlag = false
        var setProvinces : String = ""
        app = application as MainApp

        binding = ActivityGolfpoiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind the toolbar and set the title
        // binding.toolbarAdd.title = title
        binding.toolbarAdd.title = app.golfPOIData.getCurrentUser().firstName
        setSupportActionBar(binding.toolbarAdd)

        Timber.plant(Timber.DebugTree())

        // creating objects needed for the spinner drop down
        // Dropdown of Provinces taken from the strings resource file
        val provinces = resources.getStringArray(R.array.provinces)
        val spinner : Spinner = findViewById(R.id.provinceSpinner)

        // Create an ArrayAdapter object with the dropdown type
        // and populate using the list of the provinces.

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, provinces)
        spinner.adapter = adapter

        // Set the Range for the Course Par picker
        binding.golfPOIparPicker.minValue = 70
        binding.golfPOIparPicker.maxValue = 72

        i("GOLFPOI Activity started..")

        // Are we coming from the List Activity with Data being passed via Parcelize...
        if (intent.hasExtra("golfpoi_edit")) {
            golfPOI = intent.extras?.getParcelable("golfpoi_edit")!!
            binding.golfPOITitle.setText(golfPOI.courseTitle)
            binding.golfPOIDesc.setText(golfPOI.courseDescription)
            binding.golfPOIparPicker.value = golfPOI.coursePar
            binding.btnAdd.setText(R.string.button_saveGolfPOI)
            Picasso.get().load(golfPOI.image).into(binding.golfPOIImage)

            // If coming from the list of courses and Course image already set, change button text
            if (golfPOI.image != Uri.EMPTY) {
                binding.btnChooseImage.setText(R.string.change_golfPOI_image)
            }

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

        // Listener and action for the Add GOlf Course button
        binding.btnAdd.setOnClickListener() {
            golfPOI.courseTitle = binding.golfPOITitle.text.toString()
            golfPOI.courseDescription = binding.golfPOIDesc.text.toString()
            golfPOI.courseProvince = setProvinces
            golfPOI.coursePar = binding.golfPOIparPicker.value

            i("Setting the model province to $setProvinces")
            if (golfPOI.courseTitle.isNotEmpty() && golfPOI.courseDescription.isNotEmpty()) {
                if (editFlag) {
                    i("save Button Pressed ${golfPOI.courseTitle} and ${golfPOI.courseDescription}")
                    app.golfPOIData.updatePOI(golfPOI.copy())
                } else {
                    i("add Button Pressed ${golfPOI.courseTitle} and ${golfPOI.courseDescription}")
                    app.golfPOIData.createPOI(golfPOI.copy())
                }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar
                    .make(it, R.string.prompt_addGolfPOI, Snackbar.LENGTH_LONG)
                    .show()
            }

        }

        // Listener for the Add Image button
        binding.btnChooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.btnGolfPOILocation.setOnClickListener {
            i ("Set Location Pressed")
            if (golfPOI.lat != 0.0 && golfPOI.lng != 0.0) {
                location.lat = golfPOI.lat
                location.lng = golfPOI.lng
                location.zoom = golfPOI.zoom
                location.name = golfPOI.courseTitle
            }
            val launcherIntent = Intent(this, GolfPOIMapActivity::class.java)
                                .putExtra("Location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        //callback to the image picker
        registerImagePickerCallback()

        //Callback to the location selection
        registerMapCallback()
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
            R.id.item_logout -> {
                val launcherIntent = Intent(this, GolfPOILoginActivity::class.java)
                //launcherIntent.putExtra("loggedin_user", user)
                startActivityForResult(launcherIntent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Register a callback along with the contract that defines its input (and output) types
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            golfPOI.image = result.data!!.data!!
                            i("result.data.data: ${result.data!!.data!!}")
                            i("golfPOI.image: ${golfPOI.image}")
                            Picasso.get()
                                .load(golfPOI.image)
                                .into(binding.golfPOIImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    // Register Callback
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            golfPOI.lat = location.lat
                            golfPOI.lng = location.lng
                            golfPOI.zoom = location.zoom
                        }
                    }

                    RESULT_CANCELED -> { } else -> { }
                }
            }

    }
}