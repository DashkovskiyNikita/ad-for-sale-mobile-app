package com.dashkovskiy.ui.ads

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dashkovskiy.R
import com.dashkovskiy.utils.formatToPriceWithCurrency
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdDetailsScreen(
    modifier: Modifier = Modifier,
    adId: Int,
    viewModel: AdDetailsViewModel = getViewModel { parametersOf(adId) },
    navigateBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = viewModel::setFavorite
                    ) {
                        Icon(
                            painter = painterResource(
                                if (state.isFavorite)
                                    R.drawable.baseline_favorite_24
                                else
                                    R.drawable.baseline_favorite_border_24
                            ),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->


        val pagerState = rememberPagerState()

        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val ad = state.ad ?: return@Column
                AdPhotoPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(250.dp),
                    state = pagerState,
                    images = ad.photos.map { it.imageUrl }
                )
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Цена",
                        color = Color.Black
                    )
                    Text(
                        text = formatToPriceWithCurrency(ad.price, ad.currency),
                        color = Color.Black
                    )
                }
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = ad.title,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = ad.description,
                    color = Color.Black
                )
                AuthorDescription(
                    modifier = Modifier.fillMaxWidth(),
                    authorName = ad.author.name,
                    phone = ad.author.phone,
                    avatarUrl = ""
                )
            }
        }
    }
}

@Composable
fun AuthorDescription(
    modifier: Modifier = Modifier,
    authorName: String,
    phone: String,
    avatarUrl: String
) {
    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .background(Color(0xFFF8F8F8), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Продавец",
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Yellow, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_account_circle_24),
                    tint = Color.Black,
                    contentDescription = null
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = authorName,
                    color = Color.Black
                )
                Text(
                    text = phone,
                    color = Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdPhotoPager(
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(),
    images: List<String> = emptyList()
) {
    val pageState = remember {
        derivedStateOf { state.currentPage }
    }

    Box(
        modifier = modifier.background(Color.Black)
    ) {
        HorizontalPager(
            modifier = Modifier.align(Alignment.Center),
            state = state,
            verticalAlignment = Alignment.CenterVertically,
            pageCount = images.size
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .crossfade(true)
                    .data(images[page])
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .height(34.dp)
                .width(64.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
            text = "${pageState.value + 1} / ${images.size}",
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun AdDetailsPreview(){
    AdDetailsScreen(adId = 2)
}