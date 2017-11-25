package data;

import base.Range;
import data.series.*;
import data.series.grouping.GroupedByNumberDoubleSeries;
import data.series.grouping.aggregation.*;

import java.util.List;

/**
 * Created by galafit on 27/9/17.
 */
class DoubleColumn implements NumberColumn {
    DoubleSeries series;

    public DoubleColumn(DoubleSeries series) {
        this.series = series;
    }

    public DoubleColumn(double[] data) {
        this(new DoubleSeries() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public double get(int index) {
                return data[index];
            }
        });
    }

    public DoubleColumn(List<Double> data) {
        this(new DoubleSeries() {
            @Override
            public int size() {
                return data.size();
            }

            @Override
            public double get(int index) {
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
        return series.get(index);
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
        DoubleAggregateFunction aggregateFunction = new DoubleAverage();
        if(groupingType == GroupingType.FIRST) {
            aggregateFunction = new DoubleFirst();
        }

        if(groupingType == GroupingType.MAX) {
            aggregateFunction = new DoubleMax();
        }

        DoubleSeries originalSeries = series;
        series = new CachingDoubleSeries(new GroupedByNumberDoubleSeries(originalSeries, numberOfElementsInGroup, aggregateFunction));
        if(originalSeries instanceof CachingDoubleSeries) {
            // force to group and cache resultant data on the base of previous cached data
            series.size();
            // reset (disable) caching results of previous grouped data
            ((CachingDoubleSeries) originalSeries).disableCashing();
        }
    }

    @Override
    public NumberColumn copy() {
        return new DoubleColumn(series);
    }
}
