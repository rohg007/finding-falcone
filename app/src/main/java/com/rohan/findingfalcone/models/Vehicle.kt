package com.rohan.findingfalcone.models

import com.google.gson.annotations.SerializedName

data class Vehicle(
    @SerializedName("name")
    val name: String,
    @SerializedName("total_no")
    val totalNo: Int,
    @SerializedName("max_distance")
    val maxDistance: Long,
    @SerializedName("speed")
    val speed: Int
)
