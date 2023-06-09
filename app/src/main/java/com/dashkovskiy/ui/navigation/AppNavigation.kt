package com.dashkovskiy.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dashkovskiy.ui.ads.AdDetailsScreen

interface Route {
    val route: String
}

object Navigation {
    enum class AuthGraph(override val route: String) : Route {
        LOGIN("login"),
        REGISTER("register")
    }

    enum class MainGraph(override val route: String) : Route {
        ADS("ads"),
        FAVORITE("favorite"),
        USER_ADS("user_ads"),
        PROFILE("profile")
    }

    enum class AdsGraph(override val route: String) : Route {
        ADS_DETAIL("ads_detail")
    }

    val bottomBarRoutes = Navigation.MainGraph.values().map { it.route }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController()
) {
    val backStack by navHostController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: ""

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentRoute in Navigation.bottomBarRoutes) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    navigateToRoute = { destination ->
                        navHostController.navigate(destination.route) {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navHostController,
            startDestination = Navigation.MainGraph.ADS.route
        ) {
            authGraph(navHostController = navHostController)
            mainGraph(navHostController = navHostController)
            composable(
                route = "${Navigation.AdsGraph.ADS_DETAIL.route}/{ad_id}",
                arguments = listOf(navArgument("ad_id") { type = NavType.IntType })
            ) { backStack ->
                val adId = backStack.arguments?.getInt("ad_id") ?: return@composable
                AdDetailsScreen(
                    adId = adId,
                    navigateBack = { navHostController.popBackStack() }
                )
            }
        }
    }
}