package data;

import grouping.GroupingFunction;

import java.util.ArrayList;

/**
 * Created by galafit on 17/7/17.
 */
public class GroupedDataPointSet<Y> implements SlidingSet<Y> {
    private RangableSet<Y> rowPoints;
    private static final int SHOULDER = 1; // additional number of elements when we calculate range start and end indexes
    private GroupingFunction<Y> groupingFunction;
    private ArrayList<GroupedDataPoint<Y>> groupedDataPoints;
    private ExtremesFunction<Y> extremesFunction;
    private double groupingInterval;

    public GroupedDataPointSet(RangableSet<Y> rowPoints) {
        this.rowPoints = rowPoints;
    }

    public void setGroupingInterval(double groupingInterval){
        this.groupingInterval = groupingInterval;
    }

    private double getClosestIntervalPrev(double value) {
        return Math.floor(value / groupingInterval) * groupingInterval;
    }

    @Override
    public void setXRange(double startXValue, double endXValue) {
        Range range = rowPoints.getIndexRange(startXValue - SHOULDER * groupingInterval, endXValue + - SHOULDER * groupingInterval);
        long startIndex = (long) range.getStart();
        long endIndex = (long) range.getEnd();

        double roundX = getClosestIntervalPrev(startXValue);

        ArrayList<Y> buffer = new ArrayList<Y>();
        groupedDataPoints = new ArrayList<GroupedDataPoint<Y>>();
        for (long i = startIndex; i <= endIndex ; i++) {
            double x = rowPoints.getX(i);
            if(x < roundX) {
                // do nothing
            }
            else if(x > roundX  && x <= roundX + groupingInterval) {
                buffer.add(rowPoints.getY(i));
            }
            else {
                int groupLength = buffer.size();
                if(groupLength > 0) {
                    groupedDataPoints.add(new GroupedDataPoint<Y>(roundX, groupingFunction.group(buffer), i - (groupLength -1), groupLength));
                    buffer.clear();
                }

                while(x > roundX + groupingInterval) {
                    roundX += groupingInterval;
                }
                buffer.add(rowPoints.getY(i));
            }
        }
    }

    @Override
    public long getFullDataSize() {
        return rowPoints.size();
    }

    @Override
    public Range getYRange() {
        if(groupedDataPoints.size() <= 0) {
            return null;
        }
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        for (long i = 0; i < groupedDataPoints.size() ; i++) {
            Range extremes = extremesFunction.getExtremes(groupedDataPoints.get((int)i).getY());
            max = Math.max(max, extremes.getEnd());
            min = Math.min(min, extremes.getStart());
        }
        return new Range(min, max);
    }
    @Override
    public Range getFullXRange() {
        return new Range(rowPoints.getX(0), rowPoints.getX(rowPoints.size() - 1));
    }


    @Override
    public long size() {
        return groupedDataPoints.size();
    }

    @Override
    public Double getX(long index) {
        return groupedDataPoints.get((int)index).getX();
    }

    @Override
    public Y getY(long index) {
        return groupedDataPoints.get((int)index).getY();
    }
}
