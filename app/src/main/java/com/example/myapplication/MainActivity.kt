package com.example.myapplication


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import coil.compose.AsyncImage
import com.example.myapplication.Local.AppRoomDatabase
import com.example.myapplication.Local.MovieDetailViewModel
import com.example.myapplication.Local.MovieViewModeldb
import com.example.myapplication.domain.Movie


import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                //MyApp(
                AppNavigation()
            }
        }
    }
}

fun resetDatabase(context: Context): AppRoomDatabase {
    // Borra la base de datos
    context.deleteDatabase("movies_db")

    // Vuelve a crear la base de datos
    return Room.databaseBuilder(
        context.applicationContext,
        AppRoomDatabase::class.java,
        "movies_db"
    ).build()
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreenContent(modifier: Modifier, onClick: (String) -> Unit, movieViewModel: MovieViewModeldb) {
    Log.d("MOVIESCREEN", "MoviesScreenContent")
    var listOfMovies by remember { mutableStateOf(listOf<Movie>()) }
    val context = LocalContext.current


    //movieViewModel.fetchData()


    val movieState by movieViewModel.state.collectAsStateWithLifecycle()


    when(movieState) {
        is MovieViewModeldb.MovieState.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }


        }
        is MovieViewModeldb.MovieState.Error -> {
            Toast.makeText(context, "Error ${(movieState as MovieViewModeldb.MovieState.Error).errorMessage}", Toast.LENGTH_SHORT).show()
        }
        is MovieViewModeldb.MovieState.Successful -> {
            listOfMovies = (movieState as MovieViewModeldb.MovieState.Successful).list
        }
    }


    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text( text = "CARTELERA")
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier) {
            items(listOfMovies.size) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    onClick = {
                        onClick(listOfMovies[it].id.toString())
                    }
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w500${listOfMovies[it].posterPath}",
                        contentDescription = "${listOfMovies[it].title} Poster",
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${listOfMovies[it].title}",
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }
}





@Composable
fun MoviesScreen(onClick : (String) -> Unit, movieViewModel: MovieViewModeldb) {
    Scaffold(
        content = {
                paddingValues -> MoviesScreenContent(
            modifier = Modifier.padding(paddingValues),
            onClick = onClick,movieViewModel)
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(onBackPressed: () -> Unit, movieId: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Detalles de la Pelicula")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            MovieDetailScreenContent(
                modifier = Modifier.padding(paddingValues),
                movieId = movieId
            )
        }
    )
}
@Composable
fun MovieDetailScreenContent(modifier: Modifier, movieId: String) {
    Log.d("MovieDetailScreenContent", "MovieDetailScreenContent UI")
    val viewModel: MovieDetailViewModel = hiltViewModel()
    var movieUI by remember { mutableStateOf(Movie(1, "", "", "")) }

    LaunchedEffect(Unit) {
        viewModel.findMovie(movieId)
    }

    val context = LocalContext.current
    fun updateUI(movieDetailState: MovieDetailViewModel.MovieDetailState) {
        when (movieDetailState) {
            is MovieDetailViewModel.MovieDetailState.Error -> {
                Toast.makeText(context, "Error ${movieDetailState.message}", Toast.LENGTH_SHORT).show()
            }
            is MovieDetailViewModel.MovieDetailState.Loading -> {
                Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
            }
            is MovieDetailViewModel.MovieDetailState.Successful -> {
                movieUI = movieDetailState.movie
            }
        }
    }

    viewModel.state.observe(
        LocalLifecycleOwner.current,
        Observer(::updateUI)
    )

    Column(
        modifier = modifier
    ) {

        // Mostrar la imagen del póster
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movieUI.posterPath}",
            contentDescription = "${movieUI.title} Poster",
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = movieUI.title)
        Text(text = movieUI.description)
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = Screens.MoviesScreen.route
    ) {
        composable(Screens.MoviesScreen.route) {
            val movieViewModel : MovieViewModeldb = hiltViewModel()


            MoviesScreen(
                onClick = {
                        movieId -> navController.navigate("${Screens.MovieDetailScreen.route}/${movieId}")
                },
                movieViewModel
            )
        }
        composable(
            route = "${Screens.MovieDetailScreen.route}/{movieId}",
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.StringType
                }
            )
        ) {
            MovieDetailScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                movieId = it.arguments?.getString("movieId")?:""
            )
        }
    }
}




sealed class Screens(val route: String) {
    object MoviesScreen : Screens("movies")
    object MovieDetailScreen: Screens("moviedetail")
}





