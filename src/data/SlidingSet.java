package data;

/**
 * Created by galafit on 18/7/17.
 */
public interface SlidingSet<Y> extends   DataPointSet<Y> {
    public void setXRange(double startXValue, double endXValue);
    public Range getFullXRange();

    public long getFullDataSize();

    public Range getYRange();

}
