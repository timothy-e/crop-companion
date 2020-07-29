package com.example.cs446_group8.data;

import androidx.annotation.NonNull;
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
public final class ProjectWithCrops {
    @Embedded
    private Project project;

    @Relation(parentColumn = "id", entityColumn = "project_id", entity = ProjectCrop.class)
    private List<ProjectCrop> projectCrops;
}
