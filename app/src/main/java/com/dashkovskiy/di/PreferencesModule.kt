package com.dashkovskiy.di

import com.dashkovskiy.PreferenceManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val preferencesModule = module {
    singleOf(::PreferenceManager)
}