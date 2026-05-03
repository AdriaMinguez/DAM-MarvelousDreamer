package com.example.marvelousdreamer.di

import android.content.Context
import androidx.room.Room
import com.example.marvelousdreamer.data.local.AppDatabase
import com.example.marvelousdreamer.data.local.dao.*
import com.example.marvelousdreamer.data.repository.AuthRepository
import com.example.marvelousdreamer.data.repository.TripRepositoryImpl
import com.example.marvelousdreamer.domain.TripRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "marvelous_dreamer_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideTripDao(db: AppDatabase): TripDao = db.tripDao()
    @Provides fun provideActivityDao(db: AppDatabase): ActivityDao = db.activityDao()
    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideAccessLogDao(db: AppDatabase): AccessLogDao = db.accessLogDao()

    @Provides @Singleton
    fun provideTripRepository(tripDao: TripDao, activityDao: ActivityDao): TripRepository =
        TripRepositoryImpl(tripDao, activityDao)

    @Provides @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        userDao: UserDao,
        accessLogDao: AccessLogDao
    ): AuthRepository = AuthRepository(auth, userDao, accessLogDao)
}
