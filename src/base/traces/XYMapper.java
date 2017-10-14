package base.traces;

import base.DataSet;
import base.Range;

/**
 * Created by galafit on 10/10/17.
 */
public class XYMapper {
    int yColumnNumber = -1;
    DataSet dataSet;

    void setDataSet(DataSet dataSet) {
        if(this.dataSet == null && yColumnNumber < 0) {
            for (int i = 0; i < dataSet.getAmountOfNumberColumns(); i++) {
                if(!dataSet.isXColumn(i)) {
                    yColumnNumber = i;
                    break;
                }
            }
        }
        this.dataSet = dataSet;
    }

    public void setYColumnNumber(int yColumnNumber) {
        this.yColumnNumber = yColumnNumber;
    }

    public int size() {
        if(yColumnNumber < 0) {
            return 0;
        }
        return dataSet.size();
    }

    public double getX(int index) {
        return dataSet.getXValue(index);
    }

    public double getY(int index) {
        return dataSet.getValue(index, yColumnNumber);
    }

    public Range getYExtremes() {
        return dataSet.getExtremes(yColumnNumber);
    }

    public Range getXExtremes() {
        return dataSet.getXExtremes();
    }

    public int findNearest(double xValue) {
        return dataSet.findNearestData(xValue);
    }
}
