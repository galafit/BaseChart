package data.series.grouping;

import data.series.IntSeries;

/**
 * Created by galafit on 12/10/17.
 */
public interface IntGroupingFunction {
    public int group(IntSeries series, int from, int length);
}
