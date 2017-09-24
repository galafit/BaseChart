package data_old;

import data.Range;
import grouping.GroupingFunction;

import java.awt.*;

/**
 * Created by galafit on 15/7/17.
 */
public class DataProcessor<Y> {
    private RangableSet<Y> rowPoints;
    private static final int SHOULDER = 0; // additional number of elements when we calculate range start and end indexes
    private long rangeStartIndex;
    private long rangeLength;
    private ExtremesFunction<Y> extremesFunction;

    private boolean isGroupingEnabled = true;
    private Grouper<Y> grouper;

    private XYSet<Y> resultantPoints;
    private Range yRange;

    // private int maxVisiblePoint = 100;
    private double pixelsPerPoint = 10;

    private double startXValue;
    private double endXValue;
    //private double areaWidth;
    private Rectangle area;

    public void setData(DataSet<Y> dataSet) {
        rowPoints = new XYRegularSet<Y>(dataSet);
    }

    public void setData(XYSet<Y> pointSet) {
        rowPoints = new XYOrderedSet<Y>(pointSet);
    }

    public void setExtremesFunction(ExtremesFunction<Y> extremesFunction) {
        this.extremesFunction = extremesFunction;
    }

    public void setGroupingFunction(GroupingFunction<Y> groupingFunction) {
        grouper = new Grouper<Y>(groupingFunction);
    }

    public void update() {
        area = null;
    }

    public void setXRange(double startXValue, double endXValue, Rectangle area) {
        if(this.area != null && this.area.width == area.width && this.startXValue == startXValue && this.endXValue == endXValue) {
           return;
        }
        this.startXValue = startXValue;
        this.endXValue = endXValue;
        this.area = area;

        rangeLength = 0;
        if(rowPoints == null || rowPoints.size() == 0) {
            return;
        }
        Range rangeIndexes = rowPoints.getIndexRange(startXValue, endXValue);
        if(rangeIndexes != null) {
            rangeStartIndex = (long) Math.max(0, rangeIndexes.start() - SHOULDER);
            long rangeEndIndex = (long) Math.min(rowPoints.size() - 1, rangeIndexes.end() + SHOULDER);
            rangeLength = rangeEndIndex - rangeStartIndex + 1;
        }

        resultantPoints = getRangedPoints();
        if(isGroupingEnabled && rangeLength > 0) {
            if(area.width / rangeLength < pixelsPerPoint) {
                double groupingInterval = calculateGroupingInterval(startXValue, endXValue, area);
                resultantPoints = grouper.groupPoints(rowPoints, rangeStartIndex, rangeLength, groupingInterval);
            }
        }
        yRange = calculateYRange(resultantPoints);
    }

    private double calculateGroupingInterval(double startXValue, double endXValue, Rectangle area) {
        return (endXValue - startXValue) * pixelsPerPoint / area.width ;
    }

    private Range calculateYRange(XYSet<Y> points) {
        if(points == null || points.size() == 0) {
            return null;
        }
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        for (long i = 0; i < points.size() ; i++) {
            Range extremes = extremesFunction.getExtremes(points.getY(i));
            max = Math.max(max, extremes.end());
            min = Math.min(min, extremes.start());
        }
        return new Range(min, max);
    }


    public Range getYRange()  {
        return yRange;
    }

    public Range getPointYRange(Y yValue) {
        return extremesFunction.getExtremes(yValue);
    }


    public double getPreferredPixelsPerUnit() {
        double pointInterval = 0;
        if(rowPoints != null) {
            if(rowPoints instanceof XYRegularSet) {
                pointInterval = ((XYRegularSet) rowPoints).getPointInterval();
            }
            else { // calculate average pointInterval
               Range xRange = getFullXRange();
               if(xRange != null && xRange.length() > 0) {
                   pointInterval = xRange.length() / rowPoints.size();
               }
            }
        }

        if(pointInterval > 0) {
            double preferredPixelPerUnit = 1 / pointInterval;
            return preferredPixelPerUnit;
        }
        return 0;
    }



    public Range getFullXRange() {
        if(rowPoints == null || rowPoints.size() == 0) {
            return null;
        }
        return new Range(rowPoints.getX(0).doubleValue(), rowPoints.getX(rowPoints.size() - 1).doubleValue());
    }


    private XYSet<Y> getRangedPoints() {
        return new XYSet<Y>() {
            @Override
            public long size() {
                return rangeLength;
            }

            @Override
            public Number getX(long index) {
                return rowPoints.getX(rangeStartIndex + index);
            }

            @Override
            public Y getY(long index) {
                return rowPoints.getY(rangeStartIndex + index);
            }
        };
    }

    public XYSet<Y> getProcessedPoints() {
       return resultantPoints;
    }


}
