package data.series.grouping;

import data.series.DoubleSeries;

/**
 * Created by galafit on 12/10/17.
 */
public class DoubleAverage implements DoubleGroupingFunction {
    @Override
    public double group(DoubleSeries series, int from, int length) {
        long sum = 0;
        for (int i = from; i < from + length; i++) {
            sum += series.get(i);
        }
        return (int)(sum / length);
    }
}
