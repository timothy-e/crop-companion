package com.example.cs446_group8.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "crops",
        indices = {
                @Index(value = "name", unique = true),
                @Index(value = "planting_order")
        })
@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public final class Crop {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "calories_per_100_gram")
    private double caloriesPer100Gram;

    @ColumnInfo(name = "type")
    @NonNull
    private CropType type;

    @ColumnInfo(name = "days")
    private int days;

    @ColumnInfo(name = "consumption_per_person_per_year")
    private double consumptionPerPersonPerYear;

    @ColumnInfo(name = "yield_per_100_sqft")
    private double yieldPer100Sqft;

    @ColumnInfo(name = "planting_order")
    private int plantingOrder;
}
