package com.crop.companion.data;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A {@link SowWithCrop} is a data type which represents the second half of the inner join from
 * {@link Project} to {@link Sow} to {@link Crop}.
 *
 * This data type represents the join from {@link Sow} to {@link Crop}.
 *
 * @see ProjectWithSows
 */
@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public final class SowWithCrop {
    @Embedded
    private Sow sow;

    @Relation(parentColumn = "crop_id", entityColumn = "id")
    private Crop crop;
}