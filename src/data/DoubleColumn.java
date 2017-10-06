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
    public int upperBound(double value, int from, int length) {
        return Processing.upperBound(series,  value, from, length);
    }

    @Override
    public int lowerBound(double value, int from, int length) {
        return Processing.upperBound(series,  value, from, length);
    }
}
