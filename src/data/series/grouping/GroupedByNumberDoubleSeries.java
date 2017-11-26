package data.series.grouping;

import data.series.DoubleSeries;
import data.series.IntSeries;
import data.series.grouping.aggregation.DoubleAggregateFunction;

/**
 * This class groups data in such a way that each group
 * has equal number of elements or data points.
 * Equal frequencies [equal height binning, quantiles] grouping.
 */
public class GroupedByNumberDoubleSeries implements DoubleSeries {
    private DoubleSeries inputSeries;
    private IntSeries groupsStartIndexes;
    private DoubleAggregateFunction aggregateFunction;

    public GroupedByNumberDoubleSeries(DoubleSeries inputSeries, int numberOfPointsInEachGroup, DoubleAggregateFunction aggregateFunction) {
        this.inputSeries = inputSeries;
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

    /**
     * Gets resultant number of groups/bins
     */
    @Override
    public int size() {
        // last point we do not count because it will change on adding data
        return groupsStartIndexes.size();
    }

    @Override
    public double get(int groupIndex) {
        return getGroupedValue(groupIndex);
    }

    protected double getGroupedValue(int groupIndex) {
        return aggregateFunction.group(inputSeries, groupsStartIndexes.get(groupIndex), groupsStartIndexes.get(groupIndex + 1) - groupsStartIndexes.get(groupIndex));
    }

}
