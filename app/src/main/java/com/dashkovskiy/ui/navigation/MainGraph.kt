package com.dashkovskiy.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dashkovskiy.ui.ads.AdsScreen
import com.dashkovskiy.ui.favorite.FavoriteAdsScreen
import com.dashkovskiy.ui.profile.UserProfileScreen
import com.dashkovskiy.ui.userads.UserAdsScreen

fun NavGraphBuilder.mainGraph(navHostController: NavHostController) {
    composable(route = Navigation.MainGraph.ADS.route) {
        AdsScreen(
            navigateToAdDetails = { ad ->
                navHostController.navigate("${Navigation.AdsGraph.ADS_DETAIL.route}/${ad.id}")
            }
        )
    }
    composable(route = Navigation.MainGraph.FAVORITE.route) {
        FavoriteAdsScreen()
    }
    composable(route = Navigation.MainGraph.USER_ADS.route) {
        UserAdsScreen()
    }
    composable(route = Navigation.MainGraph.PROFILE.route) {
        UserProfileScreen(
            navigateToLoginScreen = {
                navHostController.navigate(Navigation.AuthGraph.LOGIN.route)
            }
        )
    }
}