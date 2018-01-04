package data;

import base.Range;
import data.series.CachingIntSeries;
import data.series.IntSeries;
import data.series.Processing;
import data.series.grouping.GroupedByNumberIntSeries;
import data.series.grouping.aggregation.IntAggregateFunction;
import data.series.grouping.aggregation.IntAverage;
import data.series.grouping.aggregation.IntFirst;
import data.series.grouping.aggregation.IntMax;

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
            public int size() {
                return data.length;
            }

            @Override
            public int get(int index) {
                return data[index];
            }
        });
    }

    public IntColumn(List<Integer> data) {
        this(new IntSeries() {
            @Override
            public int size() {
                return data.size();
            }

            @Override
            public int get(int index) {
                return data.get(index);
            }
        });
    }


    @Override
    public int size() {
        return series.size();
    }

    @Override
    public float getValue(int index) {
        return series.get(index);
    }

    @Override
    public Range getExtremes(int from, int length) {
        return Processing.minMaxRange(series, from, length);
    }


    @Override
    public int upperBound(float value, int from, int length) {
        return Processing.upperBound(series, value, from, length);
    }

    @Override
    public int lowerBound(float value, int from, int length) {
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
            // force to group and cache resultant data on the base of previous cached data
            series.size();
            // reset (disable) caching results of previous grouped data
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
        return new IntColumn(series);
    }
}
