package com.example.marvelousdreamer.data.local

import androidx.room.TypeConverter
import com.example.marvelousdreamer.domain.ActivityType
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    @TypeConverter fun fromLocalDate(date: LocalDate?): Long? = date?.toEpochDay()
    @TypeConverter fun toLocalDate(epoch: Long?): LocalDate? = epoch?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter fun fromLocalTime(time: LocalTime?): Int? = time?.toSecondOfDay()
    @TypeConverter fun toLocalTime(secs: Int?): LocalTime? = secs?.let { LocalTime.ofSecondOfDay(it.toLong()) }

    @TypeConverter fun fromActivityType(type: ActivityType?): String? = type?.name
    @TypeConverter fun toActivityType(name: String?): ActivityType? = name?.let { ActivityType.valueOf(it) }
}
