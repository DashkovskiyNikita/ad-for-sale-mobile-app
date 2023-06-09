package com.dashkovskiy.ui.ads

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage

@Composable
fun AdCreateEditScreen() {

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uriList ->

        }
    )
    //todo:
    // pager with photos,
    // select photo from storage feature
    // photo loading,
    // delete concrete photo,
    // title field,
    // description field,
    // price field,currency field
}