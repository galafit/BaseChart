package base;

/**
 * Created by galafit on 10/10/17.
 */
public interface DataSet {
    /**
     * amount of Y columns
     */
    public int getYColumnsCount();

    public double getYValue(long index, int yColumnNumber);

    public double getXValue(long index);

    public String getAnnotation(long index);

    public long size();

    public Range getXExtremes();

    public Range getYExtremes(int yColumnNumber);

    public long findNearestData(double xValue);

    public double getAverageDataInterval();

}
