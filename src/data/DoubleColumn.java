package data;

import data.series.DoubleSeries;
import data.series.Processing;

/**
 * Created by galafit on 27/9/17.
 */
class DoubleColumn implements NumberColumn {
    DoubleSeries series;

    public DoubleColumn(DoubleSeries series) {
        this.series = series;
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
