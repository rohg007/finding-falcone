package com.rohan.findingfalcone.models

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token")
    val token: String
)
