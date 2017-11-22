package data;

import base.Range;
import data.series.DoubleSeries;
import data.series.IntSeries;
import data.series.Processing;
import data.series.grouping.aggregation.DoubleAverage;
import data.series.grouping.DoubleBinnedSeries;
import data.series.grouping.DoubleGroupedSeries;

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
    public void groupByNumber(int numberOfElementsInGroup) {
        series = new DoubleGroupedSeries(series, new DoubleAverage(), numberOfElementsInGroup);
    }

    @Override
    public IntSeries groupByInterval(double groupsInterval) {
        return null;
    }

    @Override
    public void groupCustom(IntSeries groupsStartIndexes) {

    }

    @Override
    public NumberColumn copy() {
        return new DoubleColumn(series);
    }
}
