package com.calyr.framework.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieRemote(
    @SerialName("original_title")
    val title: String,
    @SerialName("overview")
    val description: String,
    @SerialName("poster_path")
    val posterPath : String
)
