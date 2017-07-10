package main.chart.axis;

import java.awt.*;
import java.util.List;

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
        LinearTickProvider tickProvider = new LinearTickProvider(getMin(), getMax(), pointsPerUnit(area), units);

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

    private double pointsPerUnit(double length) {
        double min = getMin();
        double max = getMax();

        if (max == min) {return Double.NaN;}
        return length / (max - min);
    }


    private Double pointsPerUnit(Rectangle area) {
        return pointsPerUnit(getLength(area));
    }

    @Override
    protected double valueToPoint(double value, double minPoint, double length) {
        double axisMin = getMin();
        double axisMax = getMax();
        double min = minPoint;
        double max = minPoint + length;
        if (!isHorizontal()) {
            max = minPoint - length;
        }
        double point;
        if (axisMin == axisMax){
            point = min + (max - min) / 2;
        } else {
            if (isInverted()) {
                point = max - ((value - axisMin) / (axisMax - axisMin)) * (max - min);
            }
            else {
                point = min + ((value - axisMin) / (axisMax - axisMin)) * (max - min);
            }
        }
        return point;
    }

    @Override
    protected double pointToValue(double point, double minPoint, double length) {
        double axisMin = getMin();
        double axisMax = getMax();
        double min = minPoint;
        double max = minPoint + length;
        if (!isHorizontal()) {
            max = minPoint - length;
        }

        if (axisMin == axisMax){
            return axisMax;
        }
        if (isInverted()) {
            return axisMax - (point - min) / (max - min) * (axisMax - axisMin);
        }
        else {
            return axisMin + (point - min) / (max - min) * (axisMax - axisMin);
        }
    }
}
