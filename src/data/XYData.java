package data;

import base.DataSet;
import data.series.DoubleSeries;
import data.series.IntSeries;

import java.util.List;

/**
 * Created by galafit on 29/9/17.
 */
public class XYData implements Data {
    private BaseDataSet dataSet = new BaseDataSet();

    @Override
    public DataSet getDataSet() {
        return dataSet;
    }

    public void setXData(IntSeries data) {
        dataSet.setXData(data);
    }

    public void setXData(DoubleSeries data) {
        dataSet.setXData(data);
    }

    public void setXData(List<? extends Number> data) {
        dataSet.setXData(data);
    }

    public void setXData(double[] data) {
        dataSet.setXData(data);
    }

    public void setXData(int[] data) {
        dataSet.setXData(data);
    }

    public void setXData(double startXValue, double dataInterval) {
        dataSet.setXData(startXValue, dataInterval);
    }

    public void setYData(IntSeries data) {
        removeYData();
        dataSet.addYData(data);
    }

    public void setYData(DoubleSeries data) {
        removeYData();
        dataSet.addYData(data);
    }

    public void setYData(List<? extends Number> data) {
        removeYData();
        dataSet.addYData(data);
    }

    public void setYData(double[] data) {
        removeYData();
        dataSet.addYData(data);
    }

    public void setYData(int[] data) {
        removeYData();
        dataSet.addYData(data);
    }

    private void removeYData() {
        int yColumnNumber = dataSet.getYDimension();
        if(yColumnNumber > 0) {
            dataSet.removeYData(yColumnNumber - 1);
        }
    }
}
