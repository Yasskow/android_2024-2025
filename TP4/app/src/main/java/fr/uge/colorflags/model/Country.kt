package fr.uge.colorflags.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(val name: String, val code: String, val latitude: Float, val longitude: Float)
