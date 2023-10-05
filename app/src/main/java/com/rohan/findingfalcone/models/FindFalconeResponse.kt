package com.rohan.findingfalcone.models

import com.google.gson.annotations.SerializedName

data class FindFalconeResponse(
    @SerializedName("planet_name")
    val planetName: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("error")
    val error: String? = null
)