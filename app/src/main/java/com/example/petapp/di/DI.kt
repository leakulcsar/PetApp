package com.example.petapp.di

import com.example.petapp.data.InstantProvider
import com.example.petapp.data.RealInstantProvider
import com.example.petapp.data.network.AuthInterceptor
import com.example.petapp.data.network.createAuthService
import com.example.petapp.data.network.createPetService
import com.example.petapp.data.repository.PetRepositoryImpl
import com.example.petapp.data.repository.TokenRepositoryImpl
import com.example.petapp.domain.repository.PetRepository
import com.example.petapp.domain.repository.TokenRepository
import com.example.petapp.domain.usecase.GetPetByIdUseCase
import com.example.petapp.domain.usecase.GetPetsUseCase
import com.example.petapp.feature.detail.DetailViewModel
import com.example.petapp.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    factory<InstantProvider> { RealInstantProvider() }
    factory { createAuthService() }
    single<TokenRepository> {
        TokenRepositoryImpl(
            remoteDataSource = get(),
            instantProvider = get()
        )
    }
    factory { createPetService(tokenRepository = get()) }
    factory { AuthInterceptor(tokenRepository = get()) }
    single<PetRepository> { PetRepositoryImpl(remoteDataSource = get()) }
}

val domainModule = module {
    factory { GetPetsUseCase(petRepository = get()) }
    factory { GetPetByIdUseCase(petRepository = get()) }
}

val featureModule = module {
    viewModel { HomeViewModel(getPetsUseCase = get()) }
    viewModel { params ->
        DetailViewModel(
            animalId = params.get(),
            getPetByIdUseCase = get()
        )
    }
}