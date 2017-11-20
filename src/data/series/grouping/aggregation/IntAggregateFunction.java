package data.series.grouping.aggregation;

import data.series.IntSeries;

/**
 * Created by galafit on 12/10/17.
 */
public interface IntAggregateFunction {
    public int group(IntSeries series, int from, int length);
}
