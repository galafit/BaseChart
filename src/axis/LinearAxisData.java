package axis;

import java.awt.*;

/**
 * Created by hdablin on 08.04.17.
 */
public class LinearAxisData extends AxisData {
    private Double roundMin = null;
    private Double roundMax = null;


    @Override
    public TickProvider getTickProvider(Rectangle area) {
        roundMax = null;
        roundMin = null;
        String units = getUnits();
        units = getAxisViewSettings().isUnitsVisible() ? units : null;
        double max = getMin() +  area.getWidth() / getPixelsPerUnit(area);
        if(!isHorizontal()) {
            max = getMin() +  area.getHeight() / getPixelsPerUnit(area);
        }
        LinearTickProvider tickProvider = new LinearTickProvider(getMin(), max, getPixelsPerUnit(area), units);

        double tickInterval = getTicksSettings().getTickInterval();
        int ticksAmount = getTicksSettings().getTicksAmount();
        if(ticksAmount > 0) {
            tickProvider.setTicksAmount(ticksAmount);
        }
        else if( tickInterval > 0) {
            tickProvider.setTicksInterval(tickInterval);
        } else {
            tickProvider.setTickPixelInterval(getTicksSettings().getTickPixelInterval());
        }
        if(getMin() != getMax()) {
           // roundMin = tickProvider.getClosestTickPrev(getMin()).getValue();
           // roundMax = tickProvider.getClosestTickNext(getMax()).getValue();
        }

        return tickProvider;
    }


    @Override
    public double getMin() {
        if (isEndOnTick() && roundMin != null){
            return roundMin;
        }
        return super.getMin();
    }

    @Override
    public double getMax() {
        if (isEndOnTick() && roundMax != null){
            return roundMax;
        }
        return super.getMax();
    }

 /*   @Override
    public double valueToPoint(double value, Rectangle area) {
        double axisMin = getMin();
        double axisMax = getMax();
        double min = 0;
        double max = 0;
        if (isHorizontal()) {
            min = area.getX();
            max = area.getMaxX();
        }
        else  {
            max = area.getMinY();
            min = area.getMaxY();
        }

        double point;
        if (axisMin == axisMax){
            point = min + (max - min) / 2;
        } else {
            point = min + ((value - axisMin) / (axisMax - axisMin)) * (max - min);
        }

        return point;
    }

    @Override
    public double pointToValue(double point, Rectangle area) {
        double axisMin = getMin();
        double axisMax = getMax();
        double min = 0;
        double max = 0;
        if (isHorizontal()) {
            min = area.getX();
            max = area.getMaxX();
        }
        else  {
            max = area.getMinY();
            min = area.getMaxY();
        }

        if (axisMin == axisMax){
            return axisMax;
        }
        return axisMin + (point - min) / (max - min) * (axisMax - axisMin);
    }*/


    @Override
    public double valueToPoint(double value, Rectangle area) {
        double axisMin = getMin();
        double pointMin = area.getMinX();
        double pointsPerUnit = getPixelsPerUnit(area);
        if(!isHorizontal()) {
            pointMin = area.getMaxY();
            pointsPerUnit = - pointsPerUnit;
        }
        double point = pointMin + (value - axisMin) * pointsPerUnit;
        return point;
    }

    @Override
    public double pointToValue(double point, Rectangle area) {
        double axisMin = getMin();
        double pointMin = area.getMinX();
        double pointsPerUnit = getPixelsPerUnit(area);
        if(!isHorizontal()) {
            pointMin = area.getMaxY();
            pointsPerUnit = - pointsPerUnit;
        }
        double value = (point - pointMin) / pointsPerUnit + axisMin;
        return value;

    }
}
