package com.dashkovskiy.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dashkovskiy.R

enum class BottomBarItems(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val destination: Route
) {
    ADS(
        icon = R.drawable.baseline_home_24,
        title = R.string.bottom_bar_item_home,
        destination = Navigation.MainGraph.ADS
    ),
    FAVORITE_ADS(
        icon = R.drawable.baseline_favorite_24,
        title = R.string.bottom_bar_item_favorite,
        destination = Navigation.MainGraph.FAVORITE
    ),
//    USER_ADS(
//        icon = R.drawable.baseline_notes_24,
//        title = R.string.bottom_bar_item_user_ads,
//        destination = Navigation.MainGraph.USER_ADS
//    ),
    USER_PROFILE(
        icon = R.drawable.baseline_account_circle_24,
        title = R.string.bottom_bar_item_user_profile,
        destination = Navigation.MainGraph.PROFILE
    )
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    navigateToRoute: (Route) -> Unit
) {
    BottomNavigation {
        BottomBarItems.values().forEach { bottomBarItem ->
            BottomNavigationItem(
                selected = currentRoute == bottomBarItem.destination.route,
                icon = {
                    Icon(
                        painter = painterResource(bottomBarItem.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(bottomBarItem.title)
                    )
                },
                onClick = { navigateToRoute(bottomBarItem.destination) }
            )
        }
    }
}