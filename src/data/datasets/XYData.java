package data.datasets;

import data.DoubleSeries;
import data.IntSeries;
import data.Range;
import java.util.List;

/**
 * Created by galafit on 29/9/17.
 */
public class XYData {
    private DataSet dataSet = new DataSet();
    private int yColumnNumber = -1;

    public void setXData(IntSeries data) {
        dataSet.removeXSeries();
        int xColumnNumber = dataSet.addSeries(data);
        dataSet.setXColumn(xColumnNumber);
    }

    public void setXData(DoubleSeries data) {
        dataSet.removeXSeries();
        int xColumnNumber = dataSet.addSeries(data);
        dataSet.setXColumn(xColumnNumber);
    }

    public void setXData(List<? extends Number> data) {
        dataSet.removeXSeries();
        int xColumnNumber = dataSet.addSeries(data);
        dataSet.setXColumn(xColumnNumber);
    }

    public void setXData(double[] data) {
        dataSet.removeXSeries();
        int xColumnNumber = dataSet.addSeries(data);
        dataSet.setXColumn(xColumnNumber);
    }

    public void setXData(int[] data) {
        dataSet.removeXSeries();
        int xColumnNumber = dataSet.addSeries(data);
        dataSet.setXColumn(xColumnNumber);
    }

    public void setXData(double startValue, double dataInterval) {
        dataSet.removeXSeries();
        int xColumnNumber = dataSet.addSeries(startValue, dataInterval);
        dataSet.setXColumn(xColumnNumber);
    }


    public void setYData(IntSeries data) {
        if(yColumnNumber >= 0) {
            dataSet.removeNumberSeries(yColumnNumber);
        }
        yColumnNumber = dataSet.addSeries(data);
    }

    public void setYData(DoubleSeries data) {
        if(yColumnNumber >= 0) {
            dataSet.removeNumberSeries(yColumnNumber);
        }
        yColumnNumber = dataSet.addSeries(data);
    }

    public void setYData(int[] data) {
        if(yColumnNumber >= 0) {
            dataSet.removeNumberSeries(yColumnNumber);
        }
        yColumnNumber = dataSet.addSeries(data);
    }

    public void setYData(double[] data) {
        if(yColumnNumber >= 0) {
            dataSet.removeNumberSeries(yColumnNumber);
        }
        yColumnNumber = dataSet.addSeries(data);
    }

    public void setYData(List<? extends Number> data) {
        if(yColumnNumber >= 0) {
            dataSet.removeNumberSeries(yColumnNumber);
        }
        yColumnNumber = dataSet.addSeries(data);
    }


    public int size() {
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

    public int getNearestX(double xValue) {
        return dataSet.getNearestX(xValue);
    }

}
