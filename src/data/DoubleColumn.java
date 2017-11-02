package data;

import base.Range;
import data.series.DoubleSeries;
import data.series.IntSeries;
import data.series.Processing;
import data.series.grouping.DoubleAverage;
import data.series.grouping.DoubleBinnedSeries;
import data.series.grouping.DoubleGroupedSeries;
import data.series.grouping.IntGroupedSeries;

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
    public void group(int compression) {
        series = new DoubleGroupedSeries(series, new DoubleAverage(), compression);
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

    @Override
    public NumberColumn copy() {
        return new DoubleColumn(series);
    }
}
