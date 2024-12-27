package com.example.walky_talky.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.walky_talky.Screens.HomeScreen
import com.example.walky_talky.Screens.NotesInsertScreen
import com.example.walky_talky.Screens.SplashScreen

@Composable
fun NotesNavigation(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = "Splash") {
        composable("Splash"){
            SplashScreen(navHostController)
        }
        composable("homeScreen") {
            HomeScreen(navHostController)
        }
        composable("insertScreen"+"/{id}") {
            val id = it.arguments?.getString("id")
            NotesInsertScreen(navHostController,id)
        }
    }
}