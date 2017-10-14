package base;

/**
 * Created by galafit on 10/10/17.
 */
public interface DataSet {
    public double getValue(int index, int columnNumber);

    public double getXValue(int index);

    public String getString(int index, int columnNumber);

    public int size();

    public Range getXExtremes();

    public Range getExtremes(int columnNumber);

    public int findNearestData(double xValue);

    public int getAmountOfNumberColumns();

    public int getAmountOfStringColumns();

    public boolean isXColumn(int columnNumber);
}
