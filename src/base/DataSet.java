package base;

/**
 * Created by galafit on 10/10/17.
 */
public interface DataSet {
    public float getYValue(int index, int yColumnNumber);

    /**
     * amount of Y columns
     */
    public int getYColumnsCounter();

    public float getXValue(int index);

    public String getAnnotation(int index);

    public int size();

    public Range getXExtremes();

    public Range getYExtremes(int yColumnNumber);

    public int findNearestData(float xValue);

    public float getAverageDataInterval();

}
