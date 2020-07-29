package com.example.cs446_group8.data;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A {@link ProjectWithSows} is a data type which represents the first half of the inner join from
 * {@link Project} to {@link Sow} to {@link Crop}.
 *
 * This data type represents the join from {@link Project} to {@link Sow}.
 *
 * @see SowWithCrop
 */
@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public final class ProjectWithSows {
    @Embedded
    private Project project;

    @Relation(parentColumn = "id", entityColumn = "project_id", entity = Sow.class)
    private List<SowWithCrop> sows;
}
