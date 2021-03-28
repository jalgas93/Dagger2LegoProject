package com.example.daggerlegoproject.di

import android.app.Application
import com.example.daggerlegoproject.cache.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideRoomDao(db:AppDatabase) =db.roomDao()
}