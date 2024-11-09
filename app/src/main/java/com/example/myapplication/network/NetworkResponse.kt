package com.calyr.framework.network


import kotlinx.serialization.Serializable


@Serializable
data class NetworkResponse(
    val page: Int,
    val results: List<MovieRemote>
)
