package data.series.grouping;

import data.series.IntSeries;

/**
 * Created by galafit on 12/10/17.
 */
public class IntAverage implements IntGroupingFunction {
    @Override
    public int group(IntSeries series, int from, int length) {
        long sum = 0;
        for (int i = from; i < from + length; i++) {
            sum += series.get(i);
        }
        return (int)(sum / length);
    }
}
