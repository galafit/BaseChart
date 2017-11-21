package data.series.grouping;

import data.series.IntSeries;
import data.series.grouping.aggregation.IntAggregateFunction;
import data.series.grouping.aggregation.IntAverage;

/**
 *
 * Equal frequencies [equal height binning, quantiles] grouping - each group
 * has equal number of elements or data points.
 *
 */
public class GroupedByEqualFrequencyIntSeries extends GroupedIntSeries {
    private IntAggregateFunction aggregateFunction;

    public GroupedByEqualFrequencyIntSeries(IntSeries inputSeries, int numberOfElementsInEachGroup) {
        this(inputSeries, numberOfElementsInEachGroup, new IntAverage());
    }


    public GroupedByEqualFrequencyIntSeries(IntSeries inputSeries, int numberOfPointsInEachGroup,  IntAggregateFunction aggregateFunction) {
        super(inputSeries);
        this.aggregateFunction = aggregateFunction;

        groupsStartIndexes = new IntSeries() {
            @Override
            public int size() {
                return inputSeries.size() / numberOfPointsInEachGroup;
            }

            @Override
            public int get(int index) {
                return index * numberOfPointsInEachGroup;
            }
        };
    }


    @Override
    public int getStartBoundary(int groupIndex) {
        return inputSeries.get(groupsStartIndexes.get(groupIndex));
    }

    @Override
    public int getStopBoundary(int groupIndex) {
        if(groupIndex + 1 < size() - 1) {
            inputSeries.get(groupsStartIndexes.get(groupIndex + 1));
        }
        return inputSeries.get(size() - 1);
    }

    @Override
    protected int getGroupedValue(int groupIndex) {
        if(groupIndex + 1 < size() - 1) {
            return aggregateFunction.group(inputSeries, groupsStartIndexes.get(groupIndex), groupsStartIndexes.get(groupIndex + 1) - groupsStartIndexes.get(groupIndex));

        }
        return aggregateFunction.group(inputSeries, groupsStartIndexes.get(groupIndex), (inputSeries.size() - 1) - groupsStartIndexes.get(groupIndex));
    }
}
