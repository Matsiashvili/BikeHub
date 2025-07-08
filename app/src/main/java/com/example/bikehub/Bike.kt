package com.example.bikehub

data class Bike(
    val id: Int,
    val name: String,
    val description: String,
    val imageResId: Int,
    var isSelected: Boolean = false,
    var isExpanded: Boolean = false
)
