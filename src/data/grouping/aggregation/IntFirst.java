package data.grouping.aggregation;

import data.IntSeries;

/**
 * Created by galafit on 20/11/17.
 */
public class IntFirst implements IntAggregateFunction {
    @Override
    public int group(IntSeries series, long from, int length) {
        return series.get(from);
    }
}
