package com.dashkovskiy.ui.ads

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.dashkovskiy.R
import com.dashkovskiy.api.Ad
import com.dashkovskiy.utils.dateTimeDisplay
import com.dashkovskiy.utils.formatToPriceWithCurrency
import org.koin.androidx.compose.getViewModel

@Composable
fun AdsScreen(
    modifier: Modifier = Modifier,
    viewModel: AdsViewModel = getViewModel(),
    navigateToAdDetails: (Ad) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        viewModel.getFavorites()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    ) {
        SearchBar(
            value = state.search,
            onValueChanged = viewModel::setSearch,
            clearValue = viewModel::clearSearch,
            onSearch = viewModel::trySearch
        )

        val ads = viewModel.adsFlow.collectAsLazyPagingItems()

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.isSearchMode) {
                items(state.searchResult) { ad ->
                    AdItem(
                        title = ad.title,
                        price = formatToPriceWithCurrency(
                            price = ad.price,
                            currencyIso = ad.currency
                        ),
                        publishedAt = dateTimeDisplay(ad.createdAt),
                        imageUrl = ad.photos.firstOrNull()?.imageUrl.orEmpty(),
                        onFavoriteClick = {
                            viewModel.onFavoriteIntent(
                                id = ad.id,
                                isFavorite = it
                            )
                        },
                        onItemClick = { navigateToAdDetails(ad) },
                        isFavorite = state.isAdFavorite(ad.id)
                    )
                }
            } else {
                items(
                    count = ads.itemCount,
                    key = ads.itemKey { it.id },
                    contentType = ads.itemContentType { }
                ) { index ->
                    val ad = ads[index] ?: return@items
                    AdItem(
                        title = ad.title,
                        price = formatToPriceWithCurrency(
                            price = ad.price,
                            currencyIso = ad.currency
                        ),
                        publishedAt = dateTimeDisplay(ad.createdAt),
                        imageUrl = ad.photos.firstOrNull()?.imageUrl.orEmpty(),
                        onFavoriteClick = {
                            viewModel.onFavoriteIntent(
                                id = ad.id,
                                isFavorite = it
                            )
                        },
                        onItemClick = { navigateToAdDetails(ad) },
                        isFavorite = state.isAdFavorite(ad.id)
                    )
                }
            }

        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    onValueChanged: (String) -> Unit,
    clearValue: () -> Unit,
    onSearch: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        placeholder = {
            Text(text = placeholder)
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = { clearValue() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun AdItem(
    modifier: Modifier = Modifier,
    title: String,
    price: String,
    publishedAt: String,
    isUserAdItemMode: Boolean = false,
    onDeleteClick: () -> Unit = {},
    onFavoriteClick: (Boolean) -> Unit = {},
    onItemClick: () -> Unit,
    imageUrl: String = "",
    isFavorite: Boolean = false
) {
    Card(
        modifier = modifier
            .clickable { onItemClick() },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.clickable { onItemClick() }
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    text = title,
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    onClick = {
                        if (isUserAdItemMode)
                            onDeleteClick()
                        else
                            onFavoriteClick(!isFavorite)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            when {
                                isUserAdItemMode -> R.drawable.outline_delete_24
                                isFavorite -> R.drawable.baseline_favorite_24
                                else -> R.drawable.baseline_favorite_border_24
                            }
                        ),
                        contentDescription = null
                    )
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = price
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp),
                text = publishedAt
            )
        }
    }
}

@Preview
@Composable
fun AdsScreenPreview() {
    AdsScreen()
}