package axis;


import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 16.04.17.
 */
public abstract class Axis {
    protected AxisData axisData;
    protected AxisPainter axisPainter;

    public void setPixelsPerUnit(Double pointsPerUnit) {
        axisData.setPixelPerUnit(pointsPerUnit);
    }

    public double getLowerPadding() {
        return axisData.getLowerPadding();
    }

    public void setLowerPadding(double lowerPadding) {
        axisData.setLowerPadding(lowerPadding);
    }

    public double getUpperPadding() {
        return axisData.getUpperPadding();
    }

    public void setUpperPadding(double upperPadding) {
        axisData.setUpperPadding(upperPadding);
    }


    public boolean isOpposite() {
        return axisData.isOpposite();
    }

    public void setOpposite(boolean opposite) {
        axisData.setOpposite(opposite);
    }

    public boolean isEndOnTick() {return axisData.isEndOnTick();}

    public void setEndOnTick(boolean endOnTick) {
        axisData.setEndOnTick(endOnTick);
    }

    public void resetRange() {
        axisData.resetRange();
    }

    public boolean isVisible() {
        return axisData.getAxisViewSettings().isVisible();
    }

    public void setVisible(boolean isVisible) {
        axisData.getAxisViewSettings().setVisible(isVisible);
    }
    public AxisViewSettings getAxisViewSettings() {
        return axisData.getAxisViewSettings();
    }

    public TicksSettings getTicksSettings() {
        return axisData.getTicksSettings();
    }

    public GridSettings getGridSettings() {
        return axisData.getGridSettings();
    }

    public void setHorizontal(boolean isHorisontal){
        axisData.setHorizontal(isHorisontal);
    }

    public boolean isAutoScale() {
       return axisData.isAutoScale();
    }

    public void setAutoScale(boolean isAutoScale){
        axisData.setAutoScale(isAutoScale);
    }



    public double valueToPoint(double value, Rectangle area){
        return axisData.valueToPoint(value, area);
    }

    public double pointsToValue(double point, Rectangle area) {
        return axisData.pointToValue(point, area);
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
        axisData.setRange(newMin, newMax);
    }

    public double getMin() {return axisData.getMin();}

    public double getMax(Rectangle area) {
        return axisData.getMax(area);
    }

    public boolean isFixedScale() {
       return axisData.isFixedScale();
    }

    public void draw(Graphics2D g, Rectangle area, int anchorPoint){
        axisPainter.draw(g, area, anchorPoint);
    }

    public int getWidth(Graphics2D g, Rectangle area){
       return axisPainter.getAxisWidth(g, area);
    }
    public AxisViewSettings getViewSettings() {
        return axisData.getAxisViewSettings();
    }

    public boolean isHorizontal() {return axisData.isHorizontal();}

    public List<Tick> getTicks(Graphics2D g, Rectangle area) {
        return axisPainter.getTicks(g, area);
    }
}
