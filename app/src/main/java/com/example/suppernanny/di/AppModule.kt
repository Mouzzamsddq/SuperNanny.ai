package com.example.suppernanny.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

  @Provides
  fun providedFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

}