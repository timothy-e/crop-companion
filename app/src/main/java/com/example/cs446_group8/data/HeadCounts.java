package com.example.cs446_group8.data;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public final class HeadCounts {
    @NonNull
    @ColumnInfo(name = "people_january")
    private Integer peopleJanuary;
    @NonNull
    @ColumnInfo(name = "people_february")
    private Integer peopleFebruary;
    @NonNull
    @ColumnInfo(name = "people_march")
    private Integer peopleMarch;
    @NonNull
    @ColumnInfo(name = "people_april")
    private Integer peopleApril;
    @NonNull
    @ColumnInfo(name = "people_may")
    private Integer peopleMay;
    @NonNull
    @ColumnInfo(name = "people_june")
    private Integer peopleJune;
    @NonNull
    @ColumnInfo(name = "people_july")
    private Integer peopleJuly;
    @NonNull
    @ColumnInfo(name = "people_august")
    private Integer peopleAugust;
    @NonNull
    @ColumnInfo(name = "people_september")
    private Integer peopleSeptember;
    @NonNull
    @ColumnInfo(name = "people_october")
    private Integer peopleOctober;
    @NonNull
    @ColumnInfo(name = "people_november")
    private Integer peopleNovember;
    @NonNull
    @ColumnInfo(name = "people_december")
    private Integer peopleDecember;
}
