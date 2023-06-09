package com.dashkovskiy.ui.userads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dashkovskiy.api.UserAd
import com.dashkovskiy.ui.ads.AdItem
import com.dashkovskiy.utils.dateTimeDisplay
import com.dashkovskiy.utils.formatToPriceWithCurrency
import org.koin.androidx.compose.getViewModel

@Composable
fun UserAdsScreen(
    modifier: Modifier = Modifier,
    viewModel: UserAdsViewModel = getViewModel(),
    navigateToEditUserAd: (UserAd) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.userAds.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "У вас пока нет объявлений",
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        }
        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Fixed(2)
            ) {
                items(state.userAds) { userAd ->
                    AdItem(
                        title = userAd.title,
                        price = formatToPriceWithCurrency(
                            price = userAd.price,
                            currencyIso = userAd.currency
                        ),
                        isUserAdItemMode = true,
                        publishedAt = dateTimeDisplay(userAd.createdAt),
                        imageUrl = userAd.photos.firstOrNull()?.imageUrl.orEmpty(),
                        onDeleteClick = { viewModel.deleteUserAd(userAd.id) },
                        onItemClick = { navigateToEditUserAd(userAd) }
                    )
                }
            }
        }
    }
}
