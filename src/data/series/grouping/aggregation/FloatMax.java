package data.series.grouping.aggregation;

import data.series.FloatSeries;

/**
 * Created by galafit on 25/11/17.
 */
public class FloatMax implements FloatAggregateFunction {
    @Override
    public float group(FloatSeries series, long from, int length) {
        float max = series.get(from);
        for (int i = from + 1; i < from + length; i++) {
            max = Math.max(max, series.get(i));
        }
        return max;
    }
}
