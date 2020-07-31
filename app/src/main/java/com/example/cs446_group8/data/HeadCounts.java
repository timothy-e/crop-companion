package com.example.cs446_group8.data;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The head counts for each month. This is intended to be embedded in {@link Project}.
 */
@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public final class HeadCounts {

    @ColumnInfo(name = "january")
    private int january;

    @ColumnInfo(name = "february")
    private int february;

    @ColumnInfo(name = "march")
    private int march;

    @ColumnInfo(name = "april")
    private int april;

    @ColumnInfo(name = "may")
    private int may;

    @ColumnInfo(name = "june")
    private int june;

    @ColumnInfo(name = "july")
    private int july;

    @ColumnInfo(name = "august")
    private int august;

    @ColumnInfo(name = "september")
    private int september;

    @ColumnInfo(name = "october")
    private int october;

    @ColumnInfo(name = "november")
    private int november;

    @ColumnInfo(name = "december")
    private int december;

    public static HeadCounts empty() {
        return new HeadCounts();
    }

    public List<Integer> toList() {
        return Arrays.asList(
                getJanuary(), getFebruary(), getMarch(), getApril(), getMay(), getJune(),
                getJuly(), getAugust(), getSeptember(), getOctober(), getNovember(), getDecember()
        );
    }
}
