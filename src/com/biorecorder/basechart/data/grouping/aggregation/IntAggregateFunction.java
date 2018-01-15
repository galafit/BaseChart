package com.biorecorder.basechart.data.grouping.aggregation;

import com.biorecorder.basechart.data.IntSeries;

/**
 * Created by galafit on 12/10/17.
 */
public interface IntAggregateFunction {
    public int group(IntSeries series, long from, int length);
}
