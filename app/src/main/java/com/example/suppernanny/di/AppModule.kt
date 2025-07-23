package com.example.suppernanny.di

import com.example.suppernanny.data.auth.repositoryImpl.FirebaseAuthRepositoryImpl
import com.example.suppernanny.domain.auth.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


}