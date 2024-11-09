package com.example.myapplication.Local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.MovieRepository
import com.example.myapplication.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    sealed class MovieDetailState {
        object Loading : MovieDetailState()
        class Error(val message: String) : MovieDetailState()
        class Successful(val movie: Movie) : MovieDetailState()
    }

    val state: LiveData<MovieDetailState>
        get() = _state
    private val _state = MutableLiveData<MovieDetailState>()

    fun findMovie(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val movie = movieRepository.findById(id)
            withContext(Dispatchers.Main) {
                if (movie == null) {
                    _state.value = MovieDetailState.Error("No se encontró el $id")
                } else {
                    _state.value = MovieDetailState.Successful(movie)
                }
            }
        }
    }
}
