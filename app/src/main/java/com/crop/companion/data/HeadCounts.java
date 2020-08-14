package com.crop.companion.data;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import java.time.Month;
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

    public int get(Month month) {
        switch (month) {
            case JANUARY: return january;
            case FEBRUARY: return february;
            case MARCH: return march;
            case APRIL: return april;
            case MAY: return may;
            case JUNE: return june;
            case JULY: return july;
            case AUGUST: return august;
            case SEPTEMBER: return september;
            case OCTOBER: return october;
            case NOVEMBER: return november;
            case DECEMBER: return december;
            default: return 0;
        }
    }

    public void set(Month month, int value) {
        switch (month) {
            case JANUARY:
                january = value;
                break;
            case FEBRUARY:
                february = value;
                break;
            case MARCH:
                march = value;
                break;
            case APRIL:
                april = value;
                break;
            case MAY:
                may = value;
                break;
            case JUNE:
                june = value;
                break;
            case JULY:
                july = value;
                break;
            case AUGUST:
                august = value;
                break;
            case SEPTEMBER:
                september = value;
                break;
            case OCTOBER:
                october = value;
                break;
            case NOVEMBER:
                november = value;
                break;
            case DECEMBER:
                december = value;
                break;
        }
    }

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
