package com.example.cs446_group8.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A {@link Sow} happens when a {@link Crop} is used by a {@link Project}.
 */
@Entity(tableName = "sows",
        primaryKeys = {"project_id", "crop_id"},
        foreignKeys = {
                @ForeignKey(entity = Project.class, parentColumns = "id", childColumns = "project_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Crop.class, parentColumns = "id", childColumns = "crop_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("crop_id"),
                @Index("project_id"),
                @Index(value = {"project_id", "crop_id"}, unique = true),
        })
@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public final class Sow {
    @ColumnInfo(name = "project_id")
    private int projectId;
    @ColumnInfo(name = "crop_id")
    private int cropId;
}
