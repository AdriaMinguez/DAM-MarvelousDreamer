package com.example.marvelousdreamer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.marvelousdreamer.data.local.dao.*
import com.example.marvelousdreamer.data.local.entity.*

@Database(
    entities = [TripEntity::class, ActivityEntity::class, UserEntity::class, AccessLogEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao
    abstract fun accessLogDao(): AccessLogDao
}
