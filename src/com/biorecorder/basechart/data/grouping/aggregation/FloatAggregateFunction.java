package com.biorecorder.basechart.data.grouping.aggregation;

import com.biorecorder.basechart.data.FloatSeries;

/**
 * Created by galafit on 12/10/17.
 */
public interface FloatAggregateFunction {
    public float group(FloatSeries series, long from, int length);

}

