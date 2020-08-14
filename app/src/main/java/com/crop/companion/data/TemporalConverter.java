package com.crop.companion.data;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public final class TemporalConverter {
    @TypeConverter
    public static long localDateToLong(LocalDate localDate) {
        return localDate.toEpochDay();
    }

    @TypeConverter
    public static LocalDate longToLocalDate(long epochDay) {
        return LocalDate.ofEpochDay(epochDay);
    }
}
