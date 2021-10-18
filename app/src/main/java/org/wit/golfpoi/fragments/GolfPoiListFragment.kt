package org.wit.golfpoi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.golfpoi.adapter.GolfPOIAdapter
import org.wit.golfpoi.adapter.GolfPOIListener
import org.wit.golfpoi.databinding.FragmentGolfPoiListBinding
import org.wit.golfpoi.main.MainApp
import org.wit.golfpoi.models.GolfPOIModel


class GolfPoiListFragment : Fragment(), GolfPOIListener {
    lateinit var app: MainApp
    private var _fragBinding: FragmentGolfPoiListBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGolfPoiListBinding.inflate(inflater, container, false)
        val root = fragBinding?.root

        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        fragBinding.recyclerView.adapter = GolfPOIAdapter(app.golfPOIData.findAllPOIs(),this)
        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GolfPoiListFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onGolfPOIClick(golfPOI: GolfPOIModel) {
        TODO("Not yet implemented")
    }
}