package data.series.grouping;

import data.series.IntArrayList;
import data.series.IntSeries;

/**
 * Created by galafit on 20/11/17.
 */
public class GroupedByEqualIntervalIntSeries extends GroupedIntSeries {
    private int groupInterval; // value interval

    public GroupedByEqualIntervalIntSeries(IntSeries inputSeries, int length, int groupInterval) {
        super(inputSeries);
        this.groupInterval = groupInterval;
        groupsStartIndexes = new IntArrayList();
        group(length);
    }

    private void group(int length) {
        // first index always 0;
        if (groupsStartIndexes.size() == 0) {
            ((IntArrayList)groupsStartIndexes).add(0);
        }

        int lastGroupStartIndex = groupsStartIndexes.get(groupsStartIndexes.size() - 1);
        int groupLastStartValue = (inputSeries.get(lastGroupStartIndex) / groupInterval) * groupInterval;
        for (int i = lastGroupStartIndex; i < length; i++) {
            if (inputSeries.get(i) >= groupLastStartValue + groupInterval) {
                ((IntArrayList)groupsStartIndexes).add(i);
                groupLastStartValue = (inputSeries.get(i) / groupInterval) * groupInterval;
            }
        }
    }

    @Override
    public int getStartBoundary(int groupIndex) {
        return getGroupedValue(groupIndex);
    }

    @Override
    public int getStopBoundary(int groupIndex) {
        return getStartBoundary(groupIndex) + groupInterval;
    }

    @Override
    protected int getGroupedValue(int groupIndex) {
        return (inputSeries.get(groupsStartIndexes.get(groupIndex)) / groupInterval) * groupInterval;
    }

    public void update(int length) {
        group(length);
    }

    /**
     * Test method
     */
    public static void main(String args[]) {
        IntArrayList series = new IntArrayList();
        series.add(1, 2, 3, 6, 7, 10, 14, 16, 20, 100);
        GroupedByEqualIntervalIntSeries groupedSeries = new GroupedByEqualIntervalIntSeries(series, 3,3);
        IntSeries groupIndexes = groupedSeries.getGroupsStartIndexes();

        System.out.println(groupedSeries.size() + " size :"+ groupIndexes.size());
        for (int i = 0; i < groupedSeries.size() ; i++) {
            System.out.println("bin value: "+ groupedSeries.get(i)+ " bin start index: " + groupIndexes.get(i));
        }

        groupedSeries.update(series.size());
        System.out.println(groupedSeries.size() + " size :"+ groupIndexes.size());
        for (int i = 0; i < groupedSeries.size() ; i++) {
            System.out.println("bin value: "+ groupedSeries.get(i)+ " bin start index: " + groupIndexes.get(i));
        }
    }

}
