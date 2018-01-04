package data.series.grouping.aggregation;

import data.series.FloatSeries;

/**
 * Created by galafit on 20/11/17.
 */
public class FloatFirst implements FloatAggregateFunction {
    @Override
    public float group(FloatSeries series, int from, int length) {
        return series.get(from);
    }
}
