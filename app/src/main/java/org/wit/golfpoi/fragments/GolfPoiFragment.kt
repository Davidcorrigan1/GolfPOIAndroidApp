package org.wit.golfpoi.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.golfpoi.R
import org.wit.golfpoi.databinding.FragmentGolfPoiBinding
import org.wit.golfpoi.helpers.showImagePicker
import org.wit.golfpoi.main.MainApp
import org.wit.golfpoi.models.GolfPOIModel
import timber.log.Timber.i



class GolfPoiFragment : Fragment() {
    var golfPOI = GolfPOIModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private var _fragBinding: FragmentGolfPoiBinding? = null
    private val fragBinding get() = _fragBinding!!
    var editFlag = false
    var setProvinces : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragBinding = FragmentGolfPoiBinding.inflate(inflater, container, false)
        val root = fragBinding?.root

        //val golfPOI = arguments as GolfPOIModel
        val golfPOIBundle = arguments
        val golfPOI: GolfPOIModel? = golfPOIBundle?.getParcelable("golfPOI")
        i("The bundle1: ${golfPOI}")

        // creating objects needed for the spinner drop down
        // Dropdown of Provinces taken from the strings resource file
        val provinces = resources.getStringArray(R.array.provinces)
        val spinner : Spinner = fragBinding.provinceSpinner

        // Create an ArrayAdapter object with the dropdown type
        // and populate using the list of the provinces.
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, provinces)
        spinner.adapter = adapter

        // Set the Range for the Course Par picker
        fragBinding.golfPOIparPicker.minValue = 70
        fragBinding.golfPOIparPicker.maxValue = 72

        if (golfPOI != null) {
            fragBinding.golfPOITitle.setText(golfPOI.courseTitle)
            fragBinding.golfPOIDesc.setText(golfPOI.courseDescription)
            fragBinding.golfPOIparPicker.value = golfPOI.coursePar
            fragBinding.btnAdd.setText(R.string.button_saveGolfPOI)
            Picasso.get().load(golfPOI.image).into(fragBinding.golfPOIImage)

            // If coming from the list of courses and Course image already set, change button text
            if (golfPOI.image != Uri.EMPTY) {
                fragBinding.btnChooseImage.setText(R.string.change_golfPOI_image)
            }

            // check the current selected provence and default to that one!
            var spinnerPosition : Int = adapter.getPosition(golfPOI.courseProvince)
            spinner.setSelection(spinnerPosition)
            i("Setting the dropdown default from model value")

            editFlag = true

        }
        setSpinnerListener(spinner, provinces)
        registerImagePickerCallback(fragBinding, golfPOI)
        setButtonListener(fragBinding, golfPOI)

        return root
    }

    override fun onResume() {
        super.onResume()

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GolfPoiFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    // Set the listener buttons for choosing image and creating/updating the POI
    fun setButtonListener (layout: FragmentGolfPoiBinding, golfPOI: GolfPOIModel?) {
        // Listener for the Add Image button
        layout.btnChooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        // Listener and action for the Add GOlf Course button
        layout.btnAdd.setOnClickListener() {
            golfPOI!!.courseTitle = layout.golfPOITitle.text.toString()
            golfPOI.courseDescription = layout.golfPOIDesc.text.toString()
            golfPOI.courseProvince = setProvinces
            golfPOI.coursePar = layout.golfPOIparPicker.value

            i("Setting the model province to $setProvinces")
            if (golfPOI.courseTitle.isNotEmpty() && golfPOI.courseDescription.isNotEmpty()) {
                if (editFlag) {
                    i("save Button Pressed ${golfPOI.courseTitle} and ${golfPOI.courseDescription}")
                    i("Course being saved: ${golfPOI}")
                    app.golfPOIData.updatePOI(golfPOI.copy())
                } else {
                    i("add Button Pressed ${golfPOI.courseTitle} and ${golfPOI.courseDescription}")
                    app.golfPOIData.createPOI(golfPOI.copy())
                }
                var navController = it.findNavController()
                navController.navigate(R.id.action_golfPoiFragment_to_golfPoiListFragment)

            } else {
                Snackbar
                    .make(it, R.string.prompt_addGolfPOI, Snackbar.LENGTH_LONG)
                    .show()
            }

        }

    }

    fun setSpinnerListener (spinner: Spinner, provinces: Array<String>) {
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
    }

    // Register a callback along with the contract that defines its input (and output) types
    private fun registerImagePickerCallback(layout: FragmentGolfPoiBinding, golfPOI: GolfPOIModel?) {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            golfPOI!!.image = result.data!!.data!!
                            i("result.data.data: ${result.data!!.data!!}")
                            i("golfPOI.image: ${golfPOI.image}")
                            Picasso.get()
                                .load(golfPOI.image)
                                .into(layout.golfPOIImage)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}