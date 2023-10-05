package com.rohan.findingfalcone.models

import com.google.gson.annotations.SerializedName

data class FindFalconeRequest (
    @SerializedName("token")
    val token: String,
    @SerializedName("planet_names")
    val planetNames: List<String>,
    @SerializedName("vehicle_names")
    val vehicleNames: List<String>
    )