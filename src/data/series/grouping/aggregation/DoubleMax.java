package data.series.grouping.aggregation;

import data.series.DoubleSeries;

/**
 * Created by galafit on 25/11/17.
 */
public class DoubleMax implements DoubleAggregateFunction {
    @Override
    public double group(DoubleSeries series, int from, int length) {
        double max = series.get(from);
        for (int i = from + 1; i < from + length; i++) {
            max = Math.max(max, series.get(i));
        }
        return max;
    }
}
