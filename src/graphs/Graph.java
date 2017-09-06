package graphs;

import axis_old.Axis;
import data.*;
import functions.DoubleFunction;
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
    protected String graphName;


  /*  public Graph(DoubleFunction<T> function) {
        this.function = function;
    }
    public Graph(DataSet<T> dataSet) {
        setData(dataSet);
    }

    public Graph(XYSet<T> pointsSet) {
        setData(pointsSet);
    }*/

    public String getGraphName() {
        return graphName;
    }

    public void update() {
       dataProcessor.update();
   }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

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
        graphPainter.setData(dataProcessor.getProcessedPoints());
        graphPainter.setPaintingArea(area);
        graphPainter.draw(g, xAxis, yAxis);
        graphPainter.drawHover(g, xAxis, yAxis);
    }


    public XYPoint<T> getPoint(long index) {
        return graphPainter.getPoint(index);
    }


    public Range getPointYRange(T yValue) {
        return dataProcessor.getPointYRange(yValue);
    }


    public boolean setHoverPoint(long index) {
        return graphPainter.setHoverPoint(index);

    }


    public long getNearestPointIndex(double xValue) {
        return graphPainter.getNearestPointIndex(xValue);

    }

    public Color getColor(){
        return graphPainter.getSettings().getLineColor();
    }

    public TooltipItem getTooltipItem() {
        XYPoint<T> hoverPoint = graphPainter.getHoverPoint();
        if (hoverPoint == null){
            return null;
        }
        String label = graphName;
        if(label == null) {
            label = "value";
        }
        return new TooltipItem(label, hoverPoint.getY().toString(), graphPainter.getSettings().getLineColor());
    }
}
