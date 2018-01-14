package data;

import base.Range;
import data.grouping.GroupedByNumberFloatSeries;
import data.grouping.aggregation.FloatAggregateFunction;
import data.grouping.aggregation.FloatAverage;
import data.grouping.aggregation.FloatFirst;
import data.grouping.aggregation.FloatMax;

import java.text.MessageFormat;
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
            public long size() {
                return data.length;
            }

            @Override
            public float get(long index) {
                if(index > Integer.MAX_VALUE) {
                    String errorMessage = "Error. Expected: index is integer. Index = {0}, Integer.MAX_VALUE = {1}.";
                    String formattedError = MessageFormat.format(errorMessage, index, Integer.MAX_VALUE);
                    throw new IllegalArgumentException(formattedError);
                }
                return data[(int)index];
            }
        });
    }

    public FloatColumn(List<Float> data) {
        this(new FloatSeries() {
            @Override
            public long size() {
                return data.size();
            }

            @Override
            public float get(long index) {
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
        return Processing.upperBound(series,  value, from, length);
    }

    @Override
    public long lowerBound(double value, long from, int length) {
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
