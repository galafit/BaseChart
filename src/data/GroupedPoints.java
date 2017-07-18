package data;

import grouping.GroupingFunction;

import java.util.ArrayList;

/**
 * Created by galafit on 17/7/17.
 */
public class GroupedPoints<Y> implements PointsProvider<Y>{
    private static final int SHOULDER = 1; // additional number of elements when we calculate range start and end indexes
    private RangeblePoints<Y> rowPoints;
    private GroupingFunction<Y> groupingFunction;
    private ArrayList<GroupedPoint<Y>> groupedPoints;
    private ExtremesFunction<Y> extremesFunction;
    private double groupingInterval;

    public GroupedPoints(RangeblePoints<Y> rowPoints) {
        this.rowPoints = rowPoints;
    }

    public void setGroupingInterval(double groupingInterval){
        this.groupingInterval = groupingInterval;
    }

    public void setXRange(double startXValue, double endXValue) {
        Range range = rowPoints.getIndexRange(startXValue - SHOULDER * groupingInterval, endXValue + - SHOULDER * groupingInterval);
        long startIndex = (long) range.getStart();
        long endIndex = (long) range.getEnd();

        double roundX = getClosestIntervalPrev(startXValue);

        ArrayList<Y> buffer = new ArrayList<Y>();
        groupedPoints = new ArrayList<GroupedPoint<Y>>();
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
                    groupedPoints.add(new GroupedPoint<Y>(roundX, groupingFunction.group(buffer), i - (groupLength -1), groupLength));
                    buffer.clear();
                }

                while(x > roundX + groupingInterval) {
                    roundX += groupingInterval;
                }
                buffer.add(rowPoints.getY(i));
            }
        }
    }

    private double getClosestIntervalPrev(double value) {
        return Math.floor(value / groupingInterval) * groupingInterval;
    }

    public Range getYRange() {
        if(groupedPoints.size() <= 0) {
            return null;
        }
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        for (long i = 0; i < groupedPoints.size() ; i++) {
            Range extremes = extremesFunction.getExtremes(groupedPoints.get((int)i).getY());
            max = Math.max(max, extremes.getEnd());
            min = Math.min(min, extremes.getStart());
        }
        return new Range(min, max);
    }

    public Range getFullXRange() {
        return new Range(rowPoints.getX(0), rowPoints.getX(rowPoints.size() - 1));
    }


    @Override
    public long size() {
        return groupedPoints.size();
    }

    @Override
    public Double getX(long index) {
        return groupedPoints.get((int)index).getX();
    }

    @Override
    public Y getY(long index) {
        return groupedPoints.get((int)index).getY();
    }
}
