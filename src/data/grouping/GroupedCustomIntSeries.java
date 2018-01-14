package data.grouping;

import data.IntSeries;
import data.LongSeries;
import data.grouping.aggregation.IntAverage;
import data.grouping.aggregation.IntAggregateFunction;

import java.text.MessageFormat;

/**
 * This class groups data by specifying "start index" of each groupByNumber
 */
public class GroupedCustomIntSeries extends GroupedIntSeries{
    private IntAggregateFunction aggregateFunction;
     private int size = 0;

    public GroupedCustomIntSeries(IntSeries inputSeries, LongSeries groupsStartIndexes) {
        this(inputSeries, groupsStartIndexes, new IntAverage());
    }

    public GroupedCustomIntSeries(IntSeries inputSeries, LongSeries groupsStartIndexes, IntAggregateFunction aggregateFunction) {
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
    public long size() {
        checkSize();
        return size - 1;
    }

   // @Override
    public int getStartBoundary(long groupIndex) {
        return inputSeries.get(groupsStartIndexes.get(groupIndex));
    }

   // @Override
    public int getStopBoundary(long groupIndex) {
        return inputSeries.get(groupsStartIndexes.get(groupIndex + 1));
    }

    @Override
    protected int getGroupedValue(long groupIndex) {
        long numberOfGroupingElements = groupsStartIndexes.get(groupIndex + 1) - groupsStartIndexes.get(groupIndex);
        if(numberOfGroupingElements > Integer.MAX_VALUE) {
            String errorMessage = "Error during calculation of grouped value. Expected: numberOfGroupingElements is integer. NumberOfGroupingElements = {0}, Integer.MAX_VALUE = {1}.";
            String formattedError = MessageFormat.format(errorMessage, numberOfGroupingElements, Integer.MAX_VALUE);
            throw new RuntimeException(formattedError);
        }
        return aggregateFunction.group(inputSeries, groupsStartIndexes.get(groupIndex), (int) numberOfGroupingElements);
    }
}
