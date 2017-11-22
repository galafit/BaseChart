package data.series.grouping;

import data.series.IntSeries;
import data.series.grouping.aggregation.IntAggregateFunction;
import data.series.grouping.aggregation.IntAverage;

/**
 * This class groups data by specifying "start index" of each groupByNumber
 */
public class CustomGroupedIntSeries extends GroupedIntSeries{
    private IntAggregateFunction aggregateFunction;
     private int size = 0;

    public CustomGroupedIntSeries(IntSeries inputSeries, IntSeries groupsStartIndexes) {
        this(inputSeries, groupsStartIndexes, new IntAverage());
    }

    public CustomGroupedIntSeries(IntSeries inputSeries, IntSeries groupsStartIndexes,  IntAggregateFunction aggregateFunction) {
        super(inputSeries);
        this.aggregateFunction = aggregateFunction;
        this.groupsStartIndexes = groupsStartIndexes;
    }


    private void checkSize() {
        for (int i = size; i < groupsStartIndexes.size(); i++) {
            if(groupsStartIndexes.get(i) < inputSeries.size()) {
                size++;
            }
        }
    }

    @Override
    public int size() {
        checkSize();
        return size - 1;
    }

    @Override
    public int getStartBoundary(int groupIndex) {
        return inputSeries.get(groupsStartIndexes.get(groupIndex));
    }

    @Override
    public int getStopBoundary(int groupIndex) {
        return inputSeries.get(groupsStartIndexes.get(groupIndex + 1));
    }

    @Override
    protected int getGroupedValue(int groupIndex) {
        return aggregateFunction.group(inputSeries, groupsStartIndexes.get(groupIndex), groupsStartIndexes.get(groupIndex + 1) - groupsStartIndexes.get(groupIndex));
    }
}
