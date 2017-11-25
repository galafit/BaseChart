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
import java.util.function.IntToDoubleFunction;

/**
 * Created by galafit on 27/9/17.
 */
class IntColumn implements NumberColumn {
    private IntSeries series;
    private IntToDoubleFunction intToDoubleFunction;

    public IntColumn(IntSeries series, IntToDoubleFunction intToDoubleFunction) {
        this.series = series;
        this.intToDoubleFunction = intToDoubleFunction;
    }

    public IntColumn(IntSeries series) {
        this(series, null);
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
    public double getValue(int index) {
        if(intToDoubleFunction == null) {
            return series.get(index);
        }
        return intToDoubleFunction.applyAsDouble(series.get(index));
    }

    @Override
    public Range getExtremes(int from, int length) {
        return Processing.minMaxRange(series, from, length);
    }


    @Override
    public int upperBound(double value, int from, int length) {
        return Processing.upperBound(series,  value, from, length);
    }

    @Override
    public int lowerBound(double value, int from, int length) {
        return Processing.lowerBound(series,  value, from, length);
    }

    @Override
    public void groupByNumber(int numberOfElementsInGroup, GroupingType groupingType) {
        IntAggregateFunction aggregateFunction = new IntAverage();
        if(groupingType == GroupingType.FIRST) {
            aggregateFunction = new IntFirst();
        }

        if(groupingType == GroupingType.MAX) {
            aggregateFunction = new IntMax();
        }

        IntSeries originalSeries = series;
        series = new CachingIntSeries(new GroupedByNumberIntSeries(originalSeries, numberOfElementsInGroup, aggregateFunction));
        if(originalSeries instanceof CachingIntSeries) {
            // force to group and cache resultant data on the base of previous cached data
            series.size();
            // reset (disable) caching results of previous grouped data
            ((CachingIntSeries) originalSeries).disableCashing();
        }
    }

/*    @Override
    public IntSeries groupByInterval(double groupsInterval) {
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
