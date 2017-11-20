package data.series.grouping.aggregation;

import data.series.DoubleSeries;

/**
 * Created by galafit on 20/11/17.
 */
public class DoubleFirst implements DoubleAggregateFunction {
    @Override
    public double group(DoubleSeries series, int from, int length) {
        return series.get(from);
    }
}
