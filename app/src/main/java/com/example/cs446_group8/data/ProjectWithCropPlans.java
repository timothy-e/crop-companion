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

@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public final class ProjectWithCropPlans {
    @Embedded private Project project;
    @Relation(parentColumn = "id", entityColumn = "project_id")
    private List<CropPlan> cropPlans;
}
