package base;

/**
 * Created by galafit on 2/11/17.
 */
public class XYViewer {
    DataSet dataSet;

    public void setData(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public int size() {
        return dataSet.size();
    }

    public float getX(int index) {
        return dataSet.getXValue(index);
    }

    public float getY(int index) {
        return dataSet.getYValue(index, 0);
    }

    public Range getYExtremes() {
        return dataSet.getYExtremes(0);
    }

    public Range getXExtremes() {
        return dataSet.getXExtremes();
    }

    public int findNearest(float xValue) {
        return dataSet.findNearestData(xValue);
    }

}
