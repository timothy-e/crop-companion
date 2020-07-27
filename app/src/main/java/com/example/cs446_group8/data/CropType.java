package com.example.cs446_group8.data;

import androidx.room.TypeConverter;

import lombok.Getter;

public enum CropType {
    Colorful(0),
    Starch(1),
    Green(2);

    @Getter
    private int value;

    CropType(int value) {
        this.value = value;
    }

    public static final class Converter {
        @TypeConverter
        public static int cropTypeToInt(CropType cropType) {
            return cropType.getValue();
        }
        @TypeConverter
        public static CropType intToCropType(int cropType) {
            return CropType.values()[cropType];
        }
    }
}
