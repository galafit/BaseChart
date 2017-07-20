package data;

import grouping.GroupingFunction;

/**
 * Created by galafit on 15/7/17.
 */
public class DataProcessor<Y> {
    private RangableSet<Y> rowPoints;
    private static final int SHOULDER = 1; // additional number of elements when we calculate range start and end indexes
    private long rangeStartIndex;
    private long rangeLength;
    private ExtremesFunction<Y> extremesFunction;

    private boolean isGroupingEnabled = true;
    private Grouper<Y> grouper;

    private XYSet<Y> resultantPoints;

    private int maxVisiblePoint = 1000;


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

    public void setXRange(double startXValue, double endXValue) {
        rangeLength = 0;
        if(rowPoints == null || rowPoints.size() == 0) {
            return;
        }
        Range rangeIndexes = rowPoints.getIndexRange(startXValue, endXValue);
        if(rangeIndexes != null) {
            rangeStartIndex = (long) Math.max(0, rangeIndexes.getStart() - SHOULDER);
            long rangeEndIndex = (long) Math.min(rowPoints.size() - 1, rangeIndexes.getEnd() + SHOULDER);
            rangeLength = rangeEndIndex - rangeStartIndex + 1;
        }

        resultantPoints = getRangedPoints();
        if(isGroupingEnabled) {
            if(rangeLength > maxVisiblePoint) {
                grouper.setGroupingInterval(calculateGroupingInterval(startXValue, endXValue));
                resultantPoints = grouper.groupPoints(rowPoints, rangeStartIndex, rangeLength);
            }
        }
    }

    private double calculateGroupingInterval(double startXValue, double endXValue) {
        //double min = rowPoints.getX(rangeStartIndex);
        //double max = rowPoints.getX(rangeStartIndex + rangeLength - 1);
        return (endXValue - startXValue) / maxVisiblePoint;
    }

    private Range calculateYRange(XYSet<Y> points) {
        if(points == null || points.size() == 0) {
            return null;
        }
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        for (long i = 0; i < points.size() ; i++) {
            Range extremes = extremesFunction.getExtremes(points.getY(i));
            max = Math.max(max, extremes.getEnd());
            min = Math.min(min, extremes.getStart());
        }
        return new Range(min, max);
    }


    public Range getYRange()  {
        return calculateYRange(getProcessedPoints());
    }


    public long getFullDataSize() {
        if(rowPoints == null) {
            return 0;
        }
        return rowPoints.size();
    }

    public Range getFullXRange() {
        if(rowPoints == null || rowPoints.size() == 0) {
            return null;
        }
        return new Range(rowPoints.getX(0), rowPoints.getX(rowPoints.size() - 1));
    }


    private XYSet<Y> getRangedPoints() {
        return new XYSet<Y>() {
            @Override
            public long size() {
                return rangeLength;
            }

            @Override
            public Double getX(long index) {
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
