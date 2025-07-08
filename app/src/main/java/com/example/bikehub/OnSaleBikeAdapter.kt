package com.example.bikehub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bikehub.Bike

class OnSaleBikeAdapter(
    private val bikes: List<Bike>,
    private val onBikeSelected: (Bike) -> Unit
) : RecyclerView.Adapter<OnSaleBikeAdapter.BikeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_on_sale_bikes, parent, false)
        return BikeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BikeViewHolder, position: Int) {
        holder.bind(bikes[position])
    }

    override fun getItemCount() = bikes.size

    inner class BikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBikeImage: ImageView = itemView.findViewById(R.id.ivBikeImage)
        private val tvBikeName: TextView = itemView.findViewById(R.id.tvBikeName)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)

        fun bind(bike: Bike) {
            tvBikeName.text = bike.name
            tvDescription.text = bike.description
            ivBikeImage.setImageResource(bike.imageResId)

            itemView.setOnClickListener {
                onBikeSelected(bike)
            }
        }
    }
}
