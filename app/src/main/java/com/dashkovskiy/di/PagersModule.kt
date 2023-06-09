package com.dashkovskiy.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.dashkovskiy.ui.ads.AdsSource
import org.koin.dsl.module

val pagersModule = module {
    single {
        Pager(
            config = PagingConfig(15),
            pagingSourceFactory = { AdsSource(get()) }
        )
    }
}