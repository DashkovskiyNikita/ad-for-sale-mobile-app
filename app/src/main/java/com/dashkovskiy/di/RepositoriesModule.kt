package com.dashkovskiy.di

import com.dashkovskiy.repository.AdsRepository
import com.dashkovskiy.repository.AuthorizationRepository
import com.dashkovskiy.repository.FavoriteAdsRepository
import com.dashkovskiy.repository.ImageRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoriesModule = module {
    singleOf(::AdsRepository)
    singleOf(::AuthorizationRepository)
    singleOf(::FavoriteAdsRepository)
    singleOf(::ImageRepository)
}