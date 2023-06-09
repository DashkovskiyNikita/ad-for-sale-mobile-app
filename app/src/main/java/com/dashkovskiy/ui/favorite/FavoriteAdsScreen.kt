package com.dashkovskiy.ui.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dashkovskiy.api.Ad
import com.dashkovskiy.ui.ads.AdItem
import org.koin.androidx.compose.getViewModel

@Composable
fun FavoriteAdsScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteAdsViewModel = getViewModel(),
    navigateToAdDetails: (Ad) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        viewModel.getUserFavoriteAds()
    }

    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.favoriteAds.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет понравившихся товаров",
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
                items(state.favoriteAds) { favorite ->
                    AdItem(
                        title = favorite.ad.title,
                        price = favorite.ad.price.toString(),
                        publishedAt = favorite.ad.createdAt,
                        imageUrl = favorite.ad.photos.firstOrNull()?.imageUrl.orEmpty(),
                        onFavoriteClick = { viewModel.deleteFavorite(favorite.id) },
                        onItemClick = { navigateToAdDetails(favorite.ad) },
                        isFavorite = true
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FavoriteAdsScreenPreview() {
    FavoriteAdsScreen()
}