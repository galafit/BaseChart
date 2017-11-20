package data.series.grouping.aggregation;

import data.series.DoubleSeries;

/**
 * Created by galafit on 12/10/17.
 */
public interface DoubleAggregateFunction {
    public double group(DoubleSeries series, int from, int length);

}
