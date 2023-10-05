package com.rohan.findingfalcone

import com.rohan.findingfalcone.models.ApiException
import com.rohan.findingfalcone.models.FindFalconeResponse
import com.rohan.findingfalcone.models.Planet
import com.rohan.findingfalcone.models.Token
import com.rohan.findingfalcone.models.Vehicle

fun getDummyPlanets() = listOf(
    Planet(
        "Dummy1",
        100
    ),
    Planet(
        "Dummy2",
        100
    )
)

fun getDummyApiException() = ApiException(
    "Dummy"
)

fun getDummyVehicles() = listOf(
    Vehicle(
        "Dummy1",
        1,
        100L,
        2
    ),
    Vehicle(
        "Dummy2",
        1,
        50L,
        2
    )
)

fun mockFindFalconeSuccessResponse() = FindFalconeResponse(
    planetName = "Dummy",
    status = "Dummy"
)

fun mockFindFalconeErrorResponse() = FindFalconeResponse(
    error = "Dummy"
)

fun mockFindFalconeFailureResponse() = FindFalconeResponse(
    status = "Dummy"
)

val mockToken = Token("ashjkhasljapfsfjoah")