package com.biorecorder.basechart.data.grouping.aggregation;

import com.biorecorder.basechart.data.FloatSeries;

/**
 * Created by galafit on 25/11/17.
 */
public class FloatMax implements FloatAggregateFunction {
    @Override
    public float group(FloatSeries series, long from, int length) {
        float max = series.get(from);
        for (long i = from + 1; i < from + length; i++) {
            max = Math.max(max, series.get(i));
        }
        return max;
    }
}
