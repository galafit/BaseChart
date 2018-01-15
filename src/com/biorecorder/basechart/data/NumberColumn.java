package com.biorecorder.basechart.data;

import com.biorecorder.basechart.chart.Range;

/**
 * Created by galafit on 17/9/17.
 */
public interface NumberColumn {
    public long size();
    public double getValue(long index);

    public Range getExtremes(long from, int length);
    public long upperBound(double value, long from, int length);
    public long lowerBound(double value, long from, int length);
    public NumberColumn copy();
    public void setGroupingType(GroupingType groupingType);
    public void groupByNumber(int numberOfElementsInGroups, boolean isCachingEnable);

    // at the moment "grouping by equal interval" is not used. But that is draft realisation
    // just for the case we will need it in the future

   // public IntSeries groupByInterval(float groupsInterval);
    // public void groupCustom(IntSeries groupsStartIndexes);
}

