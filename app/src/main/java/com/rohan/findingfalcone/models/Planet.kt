package com.rohan.findingfalcone.models

import com.google.gson.annotations.SerializedName

data class Planet(
    @SerializedName("name")
    val name: String,
    @SerializedName("distance")
    val distance: Long
)