package dev.prateekthakur.facerecognition.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.prateekthakur.facerecognition.screens.enroll.FaceEnrollScreen
import dev.prateekthakur.facerecognition.screens.home.HomeScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    AppRoutes.navController = navController
    NavHost(navController = navController, startDestination = AppRoutes.FaceEnroll.route){
        composable(AppRoutes.HomeScreen.route){
            HomeScreen(modifier = modifier)
        }

        composable(AppRoutes.FaceEnroll.route) {
            FaceEnrollScreen{}
        }
    }
}