package data.series.grouping;

import data.series.IntArrayList;
import data.series.IntSeries;

/**
 * This class groups data dividing the hole data range on equal intervals.
 * Equal intervals [equal width binning] - each bin has equal range value or lengths.
 *
 * Черновой вариант. Заготовка на будующее если нужно будет разбивать на
 * заданные пользователем фиксированные интервалы прежде всего на временной шкале
 * (min, hour, day, month...)
 * Группирование X и Y в рамках одного DataSet предполагается
 * синхронизовать через общие groupsStart индексы.
 */
public class GroupedByEqualIntervalIntSeries extends GroupedIntSeries {
    private int groupInterval; // value interval

    public GroupedByEqualIntervalIntSeries(IntSeries inputSeries,  double groupInterval) {
        super(inputSeries);
        this.groupInterval = (int)Math.round(groupInterval);
        groupsStartIndexes = new IntArrayList();
    }

    private void group() {
        // first index always 0;
        if (groupsStartIndexes.size() == 0) {
            ((IntArrayList)groupsStartIndexes).add(0);
        }

        int lastGroupStartIndex = groupsStartIndexes.get(groupsStartIndexes.size() - 1);
        int groupLastStartValue = (inputSeries.get(lastGroupStartIndex) / groupInterval) * groupInterval;
        for (int i = lastGroupStartIndex; i < inputSeries.size(); i++) {
            if (inputSeries.get(i) >= groupLastStartValue + groupInterval) {
                ((IntArrayList)groupsStartIndexes).add(i);
                groupLastStartValue = (inputSeries.get(i) / groupInterval) * groupInterval;
            }
        }
    }

    @Override
    public int size() {
        group();
        return super.size();
    }

    @Override
    public int getStartBoundary(int groupIndex) {
        return (inputSeries.get(groupsStartIndexes.get(groupIndex)) / groupInterval) * groupInterval;
    }

    @Override
    public int getStopBoundary(int groupIndex) {
        return getStartBoundary(groupIndex) + groupInterval;
    }

    @Override
    protected int getGroupedValue(int groupIndex) {
        return getStartBoundary(groupIndex);
        // if we want middle point instead start point it will be:
        // return getStartBoundary(groupIndex) + groupInterval / 2;
    }


    /**
     * Test method
     */
    public static void main(String args[]) {
        IntArrayList series = new IntArrayList();
        series.add(1, 2, 3, 6, 7, 10, 14, 16, 20, 100);
        GroupedByEqualIntervalIntSeries groupedSeries = new GroupedByEqualIntervalIntSeries(series, 3);
        IntSeries groupIndexes = groupedSeries.getGroupsStartIndexes();

        System.out.println(groupedSeries.size() + " size :"+ groupIndexes.size());
        for (int i = 0; i < groupedSeries.size() ; i++) {
            System.out.println("groupByNumber value: "+ groupedSeries.get(i)+ " groupByNumber start index: " + groupIndexes.get(i));
        }
    }

}
