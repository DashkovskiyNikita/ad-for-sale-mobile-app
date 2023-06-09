package com.dashkovskiy.ui.ads

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkovskiy.api.AdPhoto
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.api.UpdateUserAd
import com.dashkovskiy.api.UserAd
import com.dashkovskiy.repository.AdsRepository
import com.dashkovskiy.utils.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdCreateEditState(
    val userPhotos: List<AdPhoto> = emptyList(),
    val photoUris: List<Uri> = emptyList(),
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val currency: String = "",
    val isEditMode: Boolean = false
) {
    fun mapToUpdateUserAd() =
        UpdateUserAd(
            title = title,
            description = description,
            price = price,
            currency = currency
        )
}

class AdCreateEditViewModel(
    private val adsRepository: AdsRepository,
    private val imageUtils: ImageUtils,
    userAd: UserAd?
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdCreateEditState())
    val uiState = _uiState.asStateFlow()

    private var state: AdCreateEditState
        get() = _uiState.value
        set(newState) {
            _uiState.value = newState
        }

    init {
        state = state.copy(
            title = userAd?.title.orEmpty(),
            description = userAd?.description.orEmpty(),
            price = userAd?.price?.toString().orEmpty(),
            currency = userAd?.currency.orEmpty(),
            userPhotos = userAd?.photos.orEmpty(),
            isEditMode = userAd != null
        )
    }

    fun setTitle(title: String) {
        state = state.copy(title = title)
    }

    fun setDescription(description: String) {
        state = state.copy(description = description)
    }

    fun setPrice(price: String) {
        state = state.copy(price = price)
    }

    fun setCurrency(currency: String) {
        state = state.copy(currency = currency)
    }

    fun setUserPhotos(photos: List<Uri>){
        state = state.copy(photoUris = state.photoUris + photos)
    }

//    fun uploadUserPhotos(photos: List<Uri>) {
//        viewModelScope.launch {
//            val photoContent = photos.map {
//                imageUtils.convertUriContentToByteArray(uri = it)
//            }
//            when (adsRepository.newUserAd(userAd = userAd, photos = photoContent)) {
//                is NetworkResponse.Success -> {
//                    //todo navigate to userAds screen
//                }
//                else -> {}
//            }
//        }
//    }

    fun deleteImage(){

    }

    fun updateUserAd(){

    }

    fun addUserAd(){
        viewModelScope.launch {
            val photoContent = state.photoUris.map {
                imageUtils.convertUriContentToByteArray(uri = it)
            }
            val userAd = state.mapToUpdateUserAd()
            when (adsRepository.newUserAd(userAd = userAd, photos = photoContent)) {
                is NetworkResponse.Success -> {
                    //todo navigate to userAds screen
                }
                else -> {}
            }
        }
    }

}

