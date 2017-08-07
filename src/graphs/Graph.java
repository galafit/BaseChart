package graphs;

import axis.Axis;
import data.*;
import functions.DoubleFunction;
import tooltips.TooltipInfo;
import tooltips.TooltipItem;

import java.awt.*;


/**
 * Created by hdablin on 26.04.17.
 */
public abstract class Graph<T> {
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

    public Graph(XYSet<T> pointsSet) {
        setData(pointsSet);
    }*/




    public boolean hover(int mouseX, int mouseY, Axis xAxis, Axis yAxis) {
        return graphPainter.hover(mouseX, mouseY, xAxis, yAxis);
    }

    public abstract TooltipItem getTooltipItem();

    public void setData(DataSet<T> dataSet) {
        dataProcessor.setData(dataSet);
    }

    public void setData(XYSet<T> XYSet) {
        dataProcessor.setData(XYSet);
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

    public Range getXFullRange() {
        return dataProcessor.getFullXRange();
    }

    public Range getYRange() {
        return dataProcessor.getYRange();
    }

    public double getPreferredPixelsPerUnit() {
        return dataProcessor.getPreferredPixelsPerUnit();
    }

    public void setXRange(double startXValue, double endXValue, Rectangle area) {
         dataProcessor.setXRange(startXValue, endXValue,area);
    }

    public void setLineColor(Color lineColor) {
        graphPainter.getSettings().setLineColor(lineColor);
    }

    public void draw(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        graphPainter.draw(dataProcessor.getProcessedPoints(), g, area, xAxis, yAxis);
    }

    public XYPoint<T> getHoverPoint(){
        return graphPainter.getHoverPoint();
    }

    public Range getYPixelRange(){
        return graphPainter.getYPixelRange();
    }
}
