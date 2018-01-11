package base;

/**
 * Created by galafit on 2/11/17.
 */
public class XYViewer {
    DataSet dataSet;

    public void setData(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public long size() {
        return dataSet.size();
    }

    public double getX(long index) {
        return dataSet.getXValue(index);
    }

    public double getY(long index) {
        return dataSet.getYValue(index, 0);
    }

    public Range getYExtremes() {
        return dataSet.getYExtremes(0);
    }

    public Range getXExtremes() {
        return dataSet.getXExtremes();
    }

    public long findNearest(double xValue) {
        return dataSet.findNearestData(xValue);
    }

}
