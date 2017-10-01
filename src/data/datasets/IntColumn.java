package data.datasets;

import data.IntSeries;
import data.Processing;
import data.Range;
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
    public int findNearest(double value, int from, int length) {
        int lowerBoundIndex = Processing.lowerBound(series,  value, from, length);
        if (lowerBoundIndex < from) {
            return from;
        }
        if (lowerBoundIndex == from + length - 1) {
            return lowerBoundIndex;
        }
        double distance1 = value - series.get(lowerBoundIndex);
        double distance2 = series.get(lowerBoundIndex + 1) - value;
        int nearestIndex = (distance1 < distance2) ? lowerBoundIndex : lowerBoundIndex + 1;
        return nearestIndex;
    }
}
