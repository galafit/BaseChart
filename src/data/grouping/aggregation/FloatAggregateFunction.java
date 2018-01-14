package data.grouping.aggregation;

import data.FloatSeries;

/**
 * Created by galafit on 12/10/17.
 */
public interface FloatAggregateFunction {
    public float group(FloatSeries series, long from, int length);

}

