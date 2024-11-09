package com.example.myapplication.domain

import kotlinx.serialization.Serializable

@Serializable
data class Movie(val id:Int,val title: String, val description: String,val posterPath:String)