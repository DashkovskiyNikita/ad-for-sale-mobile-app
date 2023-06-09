package com.dashkovskiy.di

import com.dashkovskiy.ui.ads.AdDetailsViewModel
import com.dashkovskiy.ui.ads.AdsViewModel
import com.dashkovskiy.ui.events.EventsContainer
import com.dashkovskiy.ui.favorite.FavoriteAdsViewModel
import com.dashkovskiy.ui.login.LoginViewModel
import com.dashkovskiy.ui.profile.UserProfileViewModel
import com.dashkovskiy.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelsModule = module {
    singleOf(::EventsContainer)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::AdsViewModel)
    viewModelOf(::FavoriteAdsViewModel)
    viewModelOf(::AdDetailsViewModel)
    viewModelOf(::UserProfileViewModel)
}