package data;

/**
 * Created by galafit on 17/7/17.
 */
public interface RangeblePoints<Y> extends PointsProvider<Y> {
    //  points inside the interval [startXValue, endXValue]
    public Range getIndexRange(double startXValue, double endXValue);
}
