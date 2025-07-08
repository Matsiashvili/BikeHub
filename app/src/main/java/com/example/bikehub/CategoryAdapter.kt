package com.example.bikehub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val categories: MutableList<Category>,
    private val onBikeSelected: (Bike) -> Unit,
    private val showCheckmarks: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_BIKE = 1
    }

    private val displayList = mutableListOf<DisplayItem>()

    init {
        rebuildDisplayList()
    }

    private fun rebuildDisplayList() {
        displayList.clear()
        for (category in categories) {
            displayList.add(DisplayItem.CategoryItem(category))
            if (category.isExpanded) {
                displayList.addAll(category.bikes.map { DisplayItem.BikeItem(it) })
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = when (displayList[position]) {
        is DisplayItem.CategoryItem -> TYPE_CATEGORY
        is DisplayItem.BikeItem -> TYPE_BIKE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_CATEGORY -> {
                val view = inflater.inflate(R.layout.item_category, parent, false)
                CategoryViewHolder(view)
            }
            TYPE_BIKE -> {
                val view = inflater.inflate(R.layout.item_bike, parent, false)
                BikeViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    override fun getItemCount(): Int = displayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = displayList[position]) {
            is DisplayItem.CategoryItem -> (holder as CategoryViewHolder).bind(item.category)
            is DisplayItem.BikeItem -> (holder as BikeViewHolder).bind(item.bike)
        }
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        private val ivExpand: ImageView = itemView.findViewById(R.id.ivExpand)
        private val recyclerViewBikes: RecyclerView = itemView.findViewById(R.id.recyclerViewBikes)

        fun bind(category: Category) {
            tvCategoryName.text = category.name
            ivExpand.rotation = if (category.isExpanded) 180f else 0f

            recyclerViewBikes.visibility = if (category.isExpanded) View.VISIBLE else View.GONE

            if (category.isExpanded) {
                recyclerViewBikes.layoutManager = LinearLayoutManager(itemView.context)
                recyclerViewBikes.adapter = BikeAdapter(
                    category.bikes.toMutableList(),
                    onBikeSelected,
                    showCheckmarks
                )
            }

            itemView.setOnClickListener {
                category.isExpanded = !category.isExpanded
                rebuildDisplayList()
            }
        }
    }

    inner class BikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBikeName: TextView = itemView.findViewById(R.id.tvBikeName)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxSelect)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val ivBikeImage: ImageView = itemView.findViewById(R.id.ivBikeImage)
        private val expandedContainer: View = itemView.findViewById(R.id.expandedContainer)

        fun bind(bike: Bike) {
            tvBikeName.text = bike.name
            tvDescription.text = bike.description
            ivBikeImage.setImageResource(bike.imageResId)
            expandedContainer.visibility = if (bike.isExpanded) View.VISIBLE else View.GONE
            checkBox.visibility = if (showCheckmarks) View.VISIBLE else View.GONE

            checkBox.setOnCheckedChangeListener(null)
            if (showCheckmarks) {
                checkBox.isChecked = bike.isSelected
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    bike.isSelected = isChecked
                    onBikeSelected(bike)
                }
            }

            itemView.setOnClickListener {
                bike.isExpanded = !bike.isExpanded
                rebuildDisplayList()
            }
        }
    }

    sealed class DisplayItem {
        data class CategoryItem(val category: Category) : DisplayItem()
        data class BikeItem(val bike: Bike) : DisplayItem()
    }
}
