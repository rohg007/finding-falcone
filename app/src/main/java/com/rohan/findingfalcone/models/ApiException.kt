package com.rohan.findingfalcone.models

import kotlin.Exception

data class ApiException (
    override val message: String,
    val statusCode: Int = 0
) : Exception(message)