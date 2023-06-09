package com.dashkovskiy.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dashkovskiy.ui.login.LoginScreen
import com.dashkovskiy.ui.register.RegisterScreen

fun NavGraphBuilder.authGraph(navHostController: NavHostController) {
    composable(route = Navigation.AuthGraph.LOGIN.route) {
        LoginScreen(
            navigateBack = { navHostController.popBackStack() },
            navigateToMainScreen = {
                navHostController.navigate(Navigation.MainGraph.PROFILE.route) {
                    popUpTo(Navigation.AuthGraph.LOGIN.route) { inclusive = true }
                }
            }
        )
    }
    composable(route = Navigation.AuthGraph.REGISTER.route) {
        RegisterScreen(
            navigateBack = { navHostController.popBackStack() }
        )
    }
}