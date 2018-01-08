package data;

import base.Range;
import data.series.*;
import data.series.grouping.GroupedByNumberFloatSeries;
import data.series.grouping.aggregation.*;

import java.util.List;

/**
 * Created by galafit on 27/9/17.
 */
class FloatColumn implements NumberColumn {
    FloatSeries series;
    private GroupingType groupingType = GroupingType.AVG;

    public FloatColumn(FloatSeries series) {
        this.series = series;
    }

    public FloatColumn(float[] data) {
        this(new FloatSeries() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public float get(int index) {
                return data[index];
            }
        });
    }

    public FloatColumn(List<Float> data) {
        this(new FloatSeries() {
            @Override
            public int size() {
                return data.size();
            }

            @Override
            public float get(int index) {
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
    public void setGroupingType(GroupingType groupingType) {
        this.groupingType = groupingType;
    }

    @Override
    public void groupByNumber(int numberOfElementsInGroup, boolean isCachingEnable) {
        FloatAggregateFunction aggregateFunction = new FloatAverage();
        if(groupingType == GroupingType.FIRST) {
            aggregateFunction = new FloatFirst();
        }

        if(groupingType == GroupingType.MAX) {
            aggregateFunction = new FloatMax();
        }

        FloatSeries originalSeries = series;
        if(isCachingEnable) {
            series = new CachingFloatSeries(new GroupedByNumberFloatSeries(originalSeries, numberOfElementsInGroup, aggregateFunction));
        } else {
            series = new GroupedByNumberFloatSeries(originalSeries, numberOfElementsInGroup, aggregateFunction);

        }
        if(originalSeries instanceof CachingFloatSeries) {
            // force to group and cache resultant data on the base of previous cached data
            series.size();
            // reset (disable) caching results of previous grouped data
            ((CachingFloatSeries) originalSeries).disableCashing();
        }
    }

    @Override
    public NumberColumn copy() {
        return new FloatColumn(series);
    }
}
