import axis.Axis;
import data.DataProcessor;
import data.DataSet;
import data.PointsList;
import data.Range;

import java.awt.*;
import java.util.function.DoubleFunction;

/**
 * Created by hdablin on 26.04.17.
 */
public abstract class Graph<T> {
    protected Color lineColor = Color.GRAY;
    protected Color fillColor = Color.GRAY;
    protected int lineWidth = 1;
    protected int pointRadious = 1;
    protected DataProcessor<T> dataProcessor = new DataProcessor<T>();

    private DoubleFunction<T> function;
    private int xAxisIndex;
    private int yAxisIndex;

/*    public Graph(DoubleFunction<T> function) {
        this.function = function;
    }
    public Graph(DataSet<T> dataSet) {
        dataProcessor = new DataProcessor<>(dataSet);
    }

    public Graph(PointsList<T> pointsList) {
        dataProcessor = new DataProcessor<T>(pointsList);
    }*/

    public void setData(DataSet<T> dataSet){
        dataProcessor.setData(dataSet);

    }

    public void setData(PointsList<T> pointsList){
        dataProcessor.setData(pointsList);

    }

    public void setFunction(DoubleFunction<T> function) {
        this.function = function;
    }

    public DoubleFunction<T> getFunction() {
        return function;
    }


    int getxAxisIndex() {
        return xAxisIndex;
    }

    int getyAxisIndex() {
        return yAxisIndex;
    }

     void setAxisIndexes(int xAxisIndex, int yAxisIndex) {
        this.xAxisIndex = xAxisIndex;
        this.yAxisIndex = yAxisIndex;
     }

    protected void rangeYaxis(Axis yAxis){
        if (yAxis.isAutoScale()){
            Range yRange = dataProcessor.getYRange();
            if(yRange != null) {
                yAxis.setRange(yRange.getStart(), yRange.getEnd());
            }
        }
    }

    protected void rangeXaxis(Axis xAxis){
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

    protected void setXRange(Rectangle area, Axis xAxis){
        double xMin = xAxis.pointsToValue(area.x, area);
        double xMax = xAxis.pointsToValue(area.x + area.width, area);
        dataProcessor.setXRange(xMin, xMax);
    }





    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public abstract void draw(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis);
}
