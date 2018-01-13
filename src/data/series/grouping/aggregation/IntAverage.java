package data.series.grouping.aggregation;

import data.series.IntSeries;

/**
 * Created by galafit on 12/10/17.
 */
public class IntAverage implements IntAggregateFunction {
    @Override
    public int group(IntSeries series, long from, int length) {
        long sum = 0;
        for (long i = from; i < from + length; i++) {
            sum += series.get(i);
        }
        return (int)(sum / length);
    }
}
