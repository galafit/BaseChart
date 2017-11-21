package data;

import base.Range;
import data.series.IntSeries;
import data.series.Processing;
import data.series.grouping.GroupedByEqualFrequencyIntSeries;

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
    public void group(int compression) {
        series = new GroupedByEqualFrequencyIntSeries(series, compression);
    }


    @Override
    public NumberColumn copy() {
        return new IntColumn(series);
    }
}
