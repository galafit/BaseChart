package data;

import base.Range;
import data.series.DoubleSeries;
import data.series.IntSeries;
import data.series.Processing;
import data.series.grouping.DoubleAverage;
import data.series.grouping.DoubleBinnedSeries;
import data.series.grouping.DoubleGroupedSeries;
import data.series.grouping.IntGroupedSeries;

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
        return Processing.lowerBound(series,  value, from, length);
    }

    @Override
    public void group(int groupInterval) {
        series = new DoubleGroupedSeries(series, new DoubleAverage(), groupInterval);
    }

    @Override
    public void group(IntSeries groupIndexes) {
        series = new DoubleGroupedSeries(series, new DoubleAverage(), groupIndexes);
    }

    @Override
    public IntSeries bin(double binInterval) {
        series = new DoubleBinnedSeries(series, binInterval);
        return ((DoubleBinnedSeries)series).bin();
    }
}
