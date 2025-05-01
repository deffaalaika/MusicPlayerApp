package com.deffa.musicplayerapp.di

import com.deffa.musicplayerapp.data.ItunesApiService
import com.deffa.musicplayerapp.data.TrackRepositoryImpl
import com.deffa.musicplayerapp.domain.SearchTrackUseCase
import com.deffa.musicplayerapp.domain.TrackRepository
import com.deffa.musicplayerapp.presentation.MainViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single {
        get<Retrofit>().create(ItunesApiService::class.java)
    }
}

val repositoryModule = module {
    single<TrackRepository> { TrackRepositoryImpl(get()) }
}

val useCaseModule = module {
    single { SearchTrackUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}