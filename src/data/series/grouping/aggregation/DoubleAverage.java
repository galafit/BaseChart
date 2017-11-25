package data.series.grouping.aggregation;

import data.series.DoubleSeries;

/**
 * Created by galafit on 12/10/17.
 */
public class DoubleAverage implements DoubleAggregateFunction {
    @Override
    public double group(DoubleSeries series, int from, int length) {
        double sum = 0;
        for (int i = from; i < from + length; i++) {
            sum += series.get(i);
        }
        return sum / length;
    }
}
