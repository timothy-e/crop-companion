package com.example.cs446_group8.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "projects",
        indices = {
                @Index(value = "name", unique = true),
        })
@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public final class Project {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "beginning_of_session")
    @NonNull
    private LocalDate beginningOfSession;

    @ColumnInfo(name = "calories_per_day_per_person")
    private int caloriesPerDayPerPerson;

    /**
     * Calories from {@link CropType#Colorful}.
     */
    @ColumnInfo(name = "calories_from_colorful")
    private int caloriesFromColorful;
    /**
     * Calories from {@link CropType#Starch}.
     */
    @ColumnInfo(name = "calories_from_starch")
    private int caloriesFromStarch;
    /**
     * Calories from {@link CropType#Green}.
     */
    @ColumnInfo(name = "calories_from_green")
    private int caloriesFromGreen;

    @Embedded(prefix = "people_")
    @NonNull
    private HeadCounts headCounts;
}
