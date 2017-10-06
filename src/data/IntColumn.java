package data;

import data.series.IntSeries;
import data.series.Processing;

import java.util.function.IntToDoubleFunction;

/**
 * Created by galafit on 27/9/17.
 */
class IntColumn implements NumberColumn {
    IntSeries series;
    IntToDoubleFunction intToDoubleFunction;

    public IntColumn(IntSeries series, IntToDoubleFunction intToDoubleFunction) {
        this.series = series;
        this.intToDoubleFunction = intToDoubleFunction;
    }

    public IntColumn(IntSeries series) {
        this(series, null);
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
        return Processing.upperBound(series,  value, from, length);
    }
}
