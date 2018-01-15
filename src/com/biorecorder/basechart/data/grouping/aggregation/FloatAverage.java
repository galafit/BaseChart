package com.biorecorder.basechart.data.grouping.aggregation;

import com.biorecorder.basechart.data.FloatSeries;

/**
 * Created by galafit on 12/10/17.
 */
public class FloatAverage implements FloatAggregateFunction {
    @Override
    public float group(FloatSeries series, long from, int length) {
        double sum = 0;
        for (long i = from; i < from + length; i++) {
            sum += series.get(i);
        }
        return (float) (sum / length);
    }
}
