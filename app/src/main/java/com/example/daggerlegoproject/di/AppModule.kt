package com.example.daggerlegoproject.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.daggerlegoproject.App
import com.example.daggerlegoproject.data.repositories.Repos
import com.example.daggerlegoproject.data.room.AppDatabase
import com.example.daggerlegoproject.presentations.FirstFragment.PagingRemoteMediator
import com.example.daggerlegoproject.retrofit.RetrofitService
import com.example.daggerlegoproject.retrofit.RetrofitService.Companion.ENDPOINT
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    //Database
//    @Singleton
//    @Provides
//    fun provideDb(app: Application) = AppDatabase.getInstance(app)

@Singleton
@Provides
fun providesRoomDatabase(app: Application): AppDatabase {
    return Room.databaseBuilder(app, AppDatabase::class.java, "jalgas")
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()
}

    @Singleton
    @Provides
    fun provideRoomDao(db: AppDatabase) = db.roomDao()

    //Repositories
    @Singleton
    @Provides
    fun provideRepos(retrofitService: RetrofitService,appDatabase: AppDatabase) = Repos(retrofitService,appDatabase)


//
//    @Singleton
//    @Provides
//     fun providesRepository(context: Context,retrofitService: RetrofitService):Repos{
//        return Repos(retrofitService, AppDatabase.getInstance(context))
//
//    }

//    @Singleton
//    @Provides
//    fun provideViewModelFactory(context: Context,retrofitService: RetrofitService):ViewModelProvider.Factory{
//        return ViewModelFactory(providesRepository(context,retrofitService))
//    }



    //Paging 3.0
    @Singleton
    @Provides
    fun provideRemoteMediator(query:String,retrofitService: RetrofitService,appDatabase: AppDatabase,token:String)
            = PagingRemoteMediator(query, retrofitService, appDatabase,token)

    //Retrofit
    @Singleton
    @Provides
    @Named("auth_token")
    fun provideToken(): String {
        return "key 45230dcfb2e26bd8ee3eae3ff16e2064"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }


    @Provides
    @Singleton
    fun provideConvertGsonFactory(gson: Gson):GsonConverterFactory{
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    fun coroutineScopeIO() = CoroutineScope(Dispatchers.IO)
    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideService(okHttpClient, converterFactory, RetrofitService::class.java)

    private fun <T> provideService(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        clazz: Class<T>
    ): T {
        return createRetrofit(okHttpClient, converterFactory).create(clazz)
    }
}