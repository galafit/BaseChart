package com.biorecorder.basechart.data;

import com.biorecorder.basechart.chart.Range;
import com.biorecorder.basechart.data.grouping.GroupedByNumberIntSeries;
import com.biorecorder.basechart.data.grouping.aggregation.IntAggregateFunction;
import com.biorecorder.basechart.data.grouping.aggregation.IntAverage;
import com.biorecorder.basechart.data.grouping.aggregation.IntFirst;
import com.biorecorder.basechart.data.grouping.aggregation.IntMax;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by galafit on 27/9/17.
 */
class IntColumn implements NumberColumn {
    private GroupingType groupingType = GroupingType.AVG;
    private IntSeries series;

    public IntColumn(IntSeries series) {
        this.series = series;
    }

    public IntColumn(int[] data) {
        this(new IntSeries() {
            @Override
            public long size() {
                return data.length;
            }

            @Override
            public int get(long index) {
                if(index > Integer.MAX_VALUE) {
                    String errorMessage = "Error. Expected index <= {0}, index = {1}.";
                    String formattedError = MessageFormat.format(errorMessage,Integer.MAX_VALUE,index);
                    throw new IllegalArgumentException(formattedError);

                }
                return data[(int)index];
            }
        });
    }

    public IntColumn(List<Integer> data) {
        this(new IntSeries() {
            @Override
            public long size() {
                return data.size();
            }

            @Override
            public int get(long index) {
                if(index > Integer.MAX_VALUE) {
                    String errorMessage = "Error. Expected: index is integer. Index = {0}, Integer.MAX_VALUE = {1}.";
                    String formattedError = MessageFormat.format(errorMessage, index, Integer.MAX_VALUE);
                    throw new IllegalArgumentException(formattedError);
                }
                return data.get((int)index);
            }
        });
    }


    @Override
    public long size() {
        return series.size();
    }

    @Override
    public double getValue(long index) {
        return series.get(index);
    }

    @Override
    public Range getExtremes(long from, int length) {
        return Processing.minMaxRange(series, from, length);
    }


    @Override
    public long upperBound(double value, long from, int length) {
        return Processing.upperBound(series, value, from, length);
    }

    @Override
    public long lowerBound(double value, long from, int length) {
        return Processing.lowerBound(series, value, from, length);
    }

    @Override
    public void setGroupingType(GroupingType groupingType) {
        this.groupingType = groupingType;
    }

    @Override
    public void groupByNumber(int numberOfElementsInGroup, boolean isCachingEnable) {
        IntAggregateFunction aggregateFunction = new IntAverage();
        if (groupingType == GroupingType.FIRST) {
            aggregateFunction = new IntFirst();
        }

        if (groupingType == GroupingType.MAX) {
            aggregateFunction = new IntMax();
        }

        IntSeries originalSeries = series;
        if (isCachingEnable) {
            series = new CachingIntSeries(new GroupedByNumberIntSeries(originalSeries, numberOfElementsInGroup, aggregateFunction));
        } else {
            series = new GroupedByNumberIntSeries(originalSeries, numberOfElementsInGroup, aggregateFunction);
        }
        if (originalSeries instanceof CachingIntSeries) {
            // force to group and cache resultant com.biorecorder.basechart.data on the com.biorecorder.basechart.chart of previous cached com.biorecorder.basechart.data
            series.size();
            // reset (disable) caching results of previous grouped com.biorecorder.basechart.data
            ((CachingIntSeries) originalSeries).disableCashing();
        }
    }

/*    @Override
    public IntSeries groupByInterval(float groupsInterval) {
        GroupedIntSeries groupedSeries = new GroupedByIntervalIntSeries(series, groupsInterval);
        series = new CachingIntSeries(groupedSeries);
        return groupedSeries.getGroupsStartIndexes();
    }

    @Override
    public void groupCustom(IntSeries groupsStartIndexes) {
        series = new CachingIntSeries(new GroupedCustomIntSeries(series, groupsStartIndexes));

    } */

    @Override
    public NumberColumn copy() {
        IntColumn newColumn = new IntColumn(series);
        newColumn.groupingType = groupingType;
        return newColumn;
    }
}
