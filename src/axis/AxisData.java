package main.chart.axis;

import java.awt.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class AxisData {
    private String name = "Test name 125679000999";
    private String units = "kg";

    private Double min = null;
    private Double max = null;
    private final double DEFAULT_MIN = 0;
    private final double DEFAULT_MAX = 10;
    private boolean isHorizontal;
    private boolean isAutoScale = true;
    private boolean isInverted = false;
    private boolean isOpposite = false;
    private double lowerPadding = 0.00;
    private double upperPadding = 0.00;
    private boolean isEndOnTick = true;


    private Double length = null;
    private Double minPoint = null;
    private Double startValue = null; // if defined it will be always put at the beginning of graph area


    private AxisViewSettings axisViewSettings = new AxisViewSettings();
    private TicksSettings ticksSettings = new TicksSettings();
    private GridSettings gridSettings = new GridSettings();

    public Double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public void setOrigin(Double origin) {
        this.minPoint = origin;
    }

    public double getLowerPadding() {
        return lowerPadding;
    }

    public void setLowerPadding(double lowerPadding) {
        this.lowerPadding = lowerPadding;
    }

    public double getUpperPadding() {
        return upperPadding;
    }

    public void setUpperPadding(double upperPadding) {
        this.upperPadding = upperPadding;
    }


    public AxisViewSettings getAxisViewSettings() {
        return axisViewSettings;
    }

    public TicksSettings getTicksSettings() {
        return ticksSettings;
    }

    public GridSettings getGridSettings() {
        return gridSettings;
    }

    public boolean isOpposite() {
        return isOpposite;
    }

    public void setOpposite(boolean opposite) {
        isOpposite = opposite;
    }

    public boolean isInverted() {
        return isInverted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public boolean isEndOnTick() {
        return isEndOnTick;
    }

    public void setEndOnTick(boolean endOnTick) {
        isEndOnTick = endOnTick;
    }

    public void resetRange() {
        min = null;
        max = null;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public double getMin() {
        if(min == null) {
            return DEFAULT_MIN;
        }
        if(min == max) {
            return min;
        }

        double resultantMin = (min == null) ? DEFAULT_MIN : min;
        double resultantMax = (max == null) ? DEFAULT_MAX : max;

        resultantMin = (!isAutoScale()) ? resultantMin : resultantMin - lowerPadding * (resultantMax - resultantMin);

        return resultantMin;

    }

    public double getMax() {
        if(max == null) {
            return DEFAULT_MAX;
        }
        if(min == max) {
            return max;
        }
        double resultantMin = (min == null) ? DEFAULT_MIN : min;
        double resultantMax = (max == null) ? DEFAULT_MAX : max;

        resultantMax = (!isAutoScale()) ? resultantMax : resultantMax + upperPadding * (resultantMax - resultantMin);
        return resultantMax;

    }

    public Double getRawMin(){
        return (min == null) ? DEFAULT_MIN : min;
    }

    public Double getRawMax(){
        return (max == null) ? DEFAULT_MAX : max;
    }

    /**
     * If isAutoScale = FALSE this method simply sets: min = newMin, max = newMax.
     * But if isAutoScale = TRUE then it only extends the range and sets:
     * min = Math.min(min, newMin), max = Math.max(max, newMax).
     *
     * @param newMin new min value
     * @param newMax new max value
     */
    public void setRange(double newMin, double newMax) {
        if (newMin > newMax){
            String errorMessage = "Error during setRange(). Expected Min < Max. Min = {0}, Max = {1}.";
            String formattedError = MessageFormat.format(errorMessage,newMin,newMax);
            throw new IllegalArgumentException(formattedError);
        }
        if (!isAutoScale) {
            min = newMin;
            max = newMax;
        } else {
            min = (min == null) ? newMin : Math.min(newMin, min);
            max = (max == null) ? newMax : Math.max(newMax, max);
        }
    }

    public abstract TickProvider getTickProvider(Rectangle area);

    public boolean isAutoScale() {
        return isAutoScale;
    }

    public void setAutoScale(boolean isAutoScale) {
        this.isAutoScale = isAutoScale;
    }

   // abstract public Double pointsPerUnit(Rectangle area);

    protected abstract double valueToPoint(double value, double minPoint, double lengt);
    protected abstract double pointToValue(double point, double minPoint, double length);

    protected double getMinPoint(Rectangle area) {
        if(minPoint != null) {
            return minPoint;
        }
        if (isHorizontal()) {
            return area.getX();
        }
        else  {
            return area.getMaxY();
        }
    }

    protected double getLength(Rectangle area) {
        double areaSize;
        if (isHorizontal()) {
            areaSize = area.getWidth();
        }
        else  {
            areaSize = area.getHeight();
        }
        if(length != null) {
            return Math.max(length, areaSize);
        }
        return areaSize;

    }


    public double valueToPoint(double value, Rectangle area) {
        return valueToPoint(value, getMinPoint(area), getLength(area));
    }

    public double pointToValue(double point, Rectangle area) {
        return pointToValue(point, getMinPoint(area), getLength(area));
    }
}
