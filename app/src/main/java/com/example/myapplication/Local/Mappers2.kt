package com.example.myapplication.Local

import com.example.myapplication.domain.Movie


fun MovieEntity.toMovie() : Movie {
    return Movie(
        id = id,
        title = title,
        description = description,
        posterPath = posterPath
    )
}




fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id, title, description, posterPath
    )
}
