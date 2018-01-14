package data.grouping.aggregation;

import data.FloatSeries;

/**
 * Created by galafit on 20/11/17.
 */
public class FloatFirst implements FloatAggregateFunction {
    @Override
    public float group(FloatSeries series, long from, int length) {
        return series.get(from);
    }
}
