package org.wit.golfpoi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.golfpoi.databinding.CardGolfpoiBinding
import org.wit.golfpoi.models.GolfPOIModel

// new interface will represent click events on the GolfPOI Card,
// and allow us to abstract the response to this event
interface GolfPOIListener {
    fun onGolfPOIClick(golfPOI: GolfPOIModel)
}


class GolfPOIAdapter constructor(private var golfPOIs: List<GolfPOIModel>,
                                private val listener: GolfPOIListener) :
    RecyclerView.Adapter<GolfPOIAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGolfpoiBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val golfPOI = golfPOIs[holder.adapterPosition]
        holder.bind(golfPOI, listener)
    }

    override fun getItemCount(): Int = golfPOIs.size

    class MainHolder(private val binding : CardGolfpoiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(golfPOI: GolfPOIModel, listener: GolfPOIListener) {
            binding.golfPOITitle.text = golfPOI.courseTitle
            binding.golfPOIDesc.text = golfPOI.courseDescription
            binding.golfPOIProvince.text = golfPOI.courseProvince
            binding.root.setOnClickListener { listener.onGolfPOIClick(golfPOI) }
        }
    }
}