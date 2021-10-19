package org.wit.golfpoi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.wit.golfpoi.databinding.FragmentGolfPoisOverviewMapBinding
import org.wit.golfpoi.main.MainApp


class GolfPoisOverviewMapFragment : Fragment() {
    lateinit var app: MainApp
    private var _fragBinding: FragmentGolfPoisOverviewMapBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGolfPoisOverviewMapBinding.inflate(inflater, container, false)
        val root = fragBinding?.root
        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GolfPoisOverviewMapFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}