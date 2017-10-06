package data;

import data.series.DoubleSeries;
import data.series.IntSeries;

import java.util.List;

/**
 * Created by galafit on 29/9/17.
 */
public class XYData implements Data {
    private DataSet dataSet = new DataSet();
    private int yColumnNumber = -1;

    @Override
    public DataSet getDataSet() {
        return dataSet;
    }

    @Override
    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

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
        dataSet.setXColumn(startValue, dataInterval);
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

    public int findNearest(double xValue) {
        return dataSet.findNearestData(xValue);
    }

}
