package com.example.marvelousdreamer.di

import android.content.Context
import androidx.room.Room
import com.example.marvelousdreamer.data.local.AppDatabase
import com.example.marvelousdreamer.data.local.dao.*
import com.example.marvelousdreamer.data.remote.api.HotelApiService
import com.example.marvelousdreamer.data.repository.AuthRepositoryImpl
import com.example.marvelousdreamer.data.repository.GalleryRepository
import com.example.marvelousdreamer.data.repository.HotelRepositoryImpl
import com.example.marvelousdreamer.data.repository.TripRepositoryImpl
import com.example.marvelousdreamer.domain.TripRepository
import com.example.marvelousdreamer.domain.repository.HotelRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val HOTELS_API_URL = "http://15.224.84.148:8090/"

    // ── Database ──────────────────────────────────────────────
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "marvelous_dreamer_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideTripDao(db: AppDatabase): TripDao = db.tripDao()
    @Provides fun provideActivityDao(db: AppDatabase): ActivityDao = db.activityDao()
    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideAccessLogDao(db: AppDatabase): AccessLogDao = db.accessLogDao()
    @Provides fun provideReservationDao(db: AppDatabase): ReservationDao = db.reservationDao()
    @Provides fun provideImageDao(db: AppDatabase): ImageDao = db.imageDao()

    // ── Trip Repository ───────────────────────────────────────
    @Provides @Singleton
    fun provideTripRepository(tripDao: TripDao, activityDao: ActivityDao): TripRepository =
        TripRepositoryImpl(tripDao, activityDao)

    // ── Firebase ──────────────────────────────────────────────
    @Provides @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, userDao: UserDao, accessLogDao: AccessLogDao): com.example.marvelousdreamer.domain.repository.AuthRepository =
        AuthRepositoryImpl(auth, userDao, accessLogDao)

    // ── Retrofit ──────────────────────────────────────────────
    @Provides @Singleton
    fun provideHotelApi(): HotelApiService =
        Retrofit.Builder()
            .baseUrl(HOTELS_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HotelApiService::class.java)

    @Provides @Singleton
    fun provideHotelRepository(api: HotelApiService): HotelRepository =
        HotelRepositoryImpl(api)

    // ── Gallery ───────────────────────────────────────────────
    @Provides @Singleton
    fun provideGalleryRepository(imageDao: ImageDao): GalleryRepository =
        GalleryRepository(imageDao)
}
