package data_old;

import data.Range;

/**
 * Created by galafit on 17/7/17.
 */
public interface RangableSet<Y> extends XYSet<Y> {
    //  points inside the interval [startXValue, endXValue]
    public Range getIndexRange(double startXValue, double endXValue);
}
