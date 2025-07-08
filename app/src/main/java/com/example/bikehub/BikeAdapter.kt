package com.example.bikehub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BikeAdapter(
    private val bikes: MutableList<Bike>,
    private val onBikeSelected: (Bike) -> Unit,
    private val showCheckmarks: Boolean = true
) : RecyclerView.Adapter<BikeAdapter.BikeViewHolder>() {

    inner class BikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBikeName: TextView = itemView.findViewById(R.id.tvBikeName)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxSelect)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val ivBikeImage: ImageView = itemView.findViewById(R.id.ivBikeImage)
        private val expandedContainer: View = itemView.findViewById(R.id.expandedContainer)

        fun bind(bike: Bike) {
            tvBikeName.text = bike.name
            checkBox.isChecked = bike.isSelected
            tvDescription.text = bike.description
            ivBikeImage.setImageResource(bike.imageResId)
            expandedContainer.visibility = if (bike.isExpanded) View.VISIBLE else View.GONE
            checkBox.visibility = if (showCheckmarks) View.VISIBLE else View.GONE

            checkBox.setOnCheckedChangeListener(null)
            if (showCheckmarks) {
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    bike.isSelected = isChecked
                    onBikeSelected(bike)
                }
            }

            itemView.setOnClickListener {
                bike.isExpanded = !bike.isExpanded
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bike, parent, false)
        return BikeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BikeViewHolder, position: Int) {
        holder.bind(bikes[position])
    }

    override fun getItemCount(): Int = bikes.size

}
