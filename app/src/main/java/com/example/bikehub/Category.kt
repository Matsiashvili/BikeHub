package com.example.bikehub


data class Category(
    val id: Int,
    val name: String,
    var bikes: MutableList<Bike>,
    var isExpanded: Boolean = false
)
