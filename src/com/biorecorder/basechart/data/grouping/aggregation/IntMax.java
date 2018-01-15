package com.biorecorder.basechart.data.grouping.aggregation;

import com.biorecorder.basechart.data.IntSeries;

/**
 * Created by galafit on 25/11/17.
 */
public class IntMax implements IntAggregateFunction {
    @Override
    public int group(IntSeries series, long from, int length) {
        int max = series.get(from);
        for (long i = from + 1; i < from + length; i++) {
            max = Math.max(max, series.get(i));
        }
        return max;
    }

}
