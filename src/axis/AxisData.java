package axis;

import java.awt.*;
import java.text.MessageFormat;

/**
 * Created by hdablin on 05.04.17.
 */
public abstract class AxisData {
    private String name = "Test name 125679000999";
    private String units = "kg";

    private final double DEFAULT_MIN = 0;
    private final double DEFAULT_MAX = 10;
    private boolean isHorizontal;
    private boolean isAutoScale = true;
    private boolean isOpposite = false;
    private double lowerPadding = 0.00;
    private double upperPadding = 0.00;
    private boolean isEndOnTick = false;

    private Double min = null;
    private Double max = null;
    private double pixelPerUnit = 0;


    private AxisViewSettings axisViewSettings = new AxisViewSettings();
    private TicksSettings ticksSettings = new TicksSettings();
    private GridSettings gridSettings = new GridSettings();


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

    public boolean isFixedScale() {
        if(pixelPerUnit != 0) {
            return true;
        }
        return false;
    }

    public double getMin() {
        if(min == null) {
            return DEFAULT_MIN;
        }
        if(min == max) {
            return min;
        }

        double resultantMin = (min == null) ? DEFAULT_MIN : min;

        resultantMin = (!isAutoScale() || isFixedScale()) ? resultantMin : resultantMin - lowerPadding * (getMax() - resultantMin);

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

    public double getMax(Rectangle area) {
        return getMin() + area.getWidth() / getPixelsPerUnit(area);
    }


    /**
     * If isAutoScale = FALSE this method simply sets: min = newMin, max = newMax.
     * But if isAutoScale = TRUE then it only extends the range and sets:
     * min = Math.min(min, newMin), max = Math.max(max, newMax).
     *
     * @param newMin new min value
     * @param newMax new max value
     */
    public void setRange(Double newMin, Double newMax) {
        if (newMin != null && newMax != null && newMin > newMax){
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


    public double getPixelsPerUnit(Rectangle area) {
        if(pixelPerUnit > 0) {
            return pixelPerUnit;
        }
        if(isHorizontal()) {
            return area.getWidth() / (getMax() - getMin());
        }
        else {
            return area.getHeight() / (getMax() - getMin());
        }
    }

    public void setPixelPerUnit(Double pixelPerUnit) {
        this.pixelPerUnit = pixelPerUnit;
    }

    public abstract double valueToPoint(double value, Rectangle area);

    public abstract double pointToValue(double point, Rectangle area);
}
