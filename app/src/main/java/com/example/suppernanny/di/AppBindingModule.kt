package com.example.suppernanny.di

import com.example.suppernanny.data.auth.repositoryImpl.FirebaseAuthRepositoryImpl
import com.example.suppernanny.domain.auth.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingModule {

  @Binds
  abstract fun bindFirebaseAuthRepo(
    impl: FirebaseAuthRepositoryImpl
  ): AuthRepository
}