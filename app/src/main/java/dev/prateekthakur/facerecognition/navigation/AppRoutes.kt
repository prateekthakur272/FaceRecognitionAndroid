package dev.prateekthakur.facerecognition.navigation

import androidx.navigation.NavHostController

sealed class AppRoutes(val route: String) {
    companion object{
        lateinit var navController: NavHostController
    }

    data object HomeScreen: AppRoutes("home")
    data object FaceEnroll: AppRoutes("enroll")
}