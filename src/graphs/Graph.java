package graphs;

import axis.Axis;
import data.*;

import java.awt.*;
import java.util.function.DoubleFunction;

/**
 * Created by hdablin on 26.04.17.
 */
public class Graph<T> {
    private DoubleFunction<T> function;
    private int xAxisIndex;
    private int yAxisIndex;
    protected DataProcessor<T> dataProcessor = new DataProcessor<T>();
    protected GraphPainter<T> graphPainter;


  /*  public Graph(DoubleFunction<T> function) {
        this.function = function;
    }
    public Graph(DataSet<T> dataSet) {
        setData(dataSet);
    }

    public Graph(DataPointSet<T> pointsSet) {
        setData(pointsSet);
    }*/


    public void setData(DataSet<T> dataSet){
        dataProcessor.setData(dataSet);

    }

    public void setData(DataPointSet<T> dataPointSet){
        dataProcessor.setData(dataPointSet);

    }

    public void setFunction(DoubleFunction<T> function) {
        this.function = function;
    }

    public DoubleFunction<T> getFunction() {
        return function;
    }


    public int getXAxisIndex() {
        return xAxisIndex;
    }

    public int getYAxisIndex() {
        return yAxisIndex;
    }

     public void setAxisIndexes(int xAxisIndex, int yAxisIndex) {
        this.xAxisIndex = xAxisIndex;
        this.yAxisIndex = yAxisIndex;
     }

    public void rangeYaxis(Axis yAxis){
        if (yAxis.isAutoScale()){
            Range yRange = dataProcessor.getYRange();
            if(yRange != null) {
                yAxis.setRange(yRange.getStart(), yRange.getEnd());
            }
        }
    }

    public void rangeXaxis(Axis xAxis){
        if (xAxis.isAutoScale()){
            Range xRange = dataProcessor.getFullXRange();
            if(xRange != null) {
                xAxis.setRange(xRange.getStart(), xRange.getEnd());
            }
        }
    }

    public long getDataSize() {
        return dataProcessor.getFullDataSize();
    }

    public void setXRange(Rectangle area, Axis xAxis){
        double xMin = xAxis.pointsToValue(area.x, area);
        double xMax = xAxis.pointsToValue(area.x + area.width, area);
        dataProcessor.setXRange(xMin, xMax);
    }


    public void setLineColor(Color lineColor) {
        graphPainter.getSettings().setLineColor(lineColor);
    }

    public void draw(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        graphPainter.draw(dataProcessor.getProcessedPoints(), g, area, xAxis, yAxis);
    }
}
