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

    public Grouper(GroupingFunction<Y> groupingFunction) {
        this.groupingFunction = groupingFunction;
    }

    private double getClosestIntervalPrev(double value, double groupingInterval) {
        return Math.floor(value / groupingInterval) * groupingInterval;
    }

    public XYSet<Y> groupPoints(XYSet<Y> points, long startIndex, long length, double groupingInterval) {
        ArrayList<Y> buffer = new ArrayList<Y>();
        if(length > 0) {
            double roundX = getClosestIntervalPrev(points.getX(0).doubleValue(), groupingInterval) + groupingInterval ;
            groupedPoints = new ArrayList<GroupedXYPoint<Y>>();
            for (long i = startIndex; i < startIndex + length; i++) {
                double x = points.getX(i).doubleValue();
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
        return new XYSet<Y>() {
            @Override
            public long size() {
                return groupedPoints.size();
            }

            @Override
            public Number getX(long index) {
                return groupedPoints.get((int)index).getX();
            }

            @Override
            public Y getY(long index) {
                return groupedPoints.get((int)index).getY();
            }
        };
    }



}
