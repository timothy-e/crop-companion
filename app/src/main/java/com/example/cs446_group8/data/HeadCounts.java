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

@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public final class HeadCounts {
    @ColumnInfo(name = "people_january")
    private int peopleJanuary;
    @ColumnInfo(name = "people_february")
    private int peopleFebruary;
    @ColumnInfo(name = "people_march")
    private int peopleMarch;
    @ColumnInfo(name = "people_april")
    private int peopleApril;
    @ColumnInfo(name = "people_may")
    private int peopleMay;
    @ColumnInfo(name = "people_june")
    private int peopleJune;
    @ColumnInfo(name = "people_july")
    private int peopleJuly;
    @ColumnInfo(name = "people_august")
    private int peopleAugust;
    @ColumnInfo(name = "people_september")
    private int peopleSeptember;
    @ColumnInfo(name = "people_october")
    private int peopleOctober;
    @ColumnInfo(name = "people_november")
    private int peopleNovember;
    @ColumnInfo(name = "people_december")
    private int peopleDecember;
}
