package data;

import grouping.GroupingFunction;

import java.util.ArrayList;

/**
 * Created by galafit on 19/7/17.
 */
public class Grouper<Y> {
    private static final int SHOULDER = 1; // additional number of elements when we calculate range start and end indexes
    private GroupingFunction<Y> groupingFunction;
    private ArrayList<GroupedXYPoint<Y>> groupedPoints;
    private double groupingInterval;

    public Grouper(GroupingFunction<Y> groupingFunction) {
        this.groupingFunction = groupingFunction;
    }

    public void setGroupingInterval(double groupingInterval){
        this.groupingInterval = groupingInterval;
    }

    private double getClosestIntervalPrev(double value) {
        return Math.floor(value / groupingInterval) * groupingInterval;
    }

    public void groupPoints(XYSet<Y> points, long startIndex, long length) {
        ArrayList<Y> buffer = new ArrayList<Y>();
        if(points.size() > 0) {
            double roundX = getClosestIntervalPrev(points.getX(0));
            groupedPoints = new ArrayList<GroupedXYPoint<Y>>();
            for (long i = startIndex; i < startIndex + length; i++) {
                double x = points.getX(i);
                if(x < roundX) {
                    // do nothing
                }
                else if(x > roundX  && x <= roundX + groupingInterval) {
                    buffer.add(points.getY(i));
                }
                else {
                    int groupLength = buffer.size();
                    if(groupLength > 0) {
                        groupedPoints.add(new GroupedXYPoint<Y>(roundX, groupingFunction.group(buffer), i - (groupLength -1), groupLength));
                        buffer.clear();
                    }

                    while(x > roundX + groupingInterval) {
                        roundX += groupingInterval;
                    }
                    buffer.add(points.getY(i));
                }
            }

        }
    }

    public XYSet<Y> getGroupedPoints() {
        return new XYSet<Y>() {
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
        };
    }

}
