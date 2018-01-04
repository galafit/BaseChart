package data.series.grouping;

import data.series.FloatSeries;
import data.series.IntSeries;
import data.series.grouping.aggregation.FloatAggregateFunction;

/**
 * This class groups data in such a way that each group
 * has equal number of elements or data points.
 * Equal frequencies [equal height binning, quantiles] grouping.
 */
public class GroupedByNumberFloatSeries implements FloatSeries {
    private FloatSeries inputSeries;
    private IntSeries groupsStartIndexes;
    private FloatAggregateFunction aggregateFunction;

    public GroupedByNumberFloatSeries(FloatSeries inputSeries, int numberOfPointsInEachGroup, FloatAggregateFunction aggregateFunction) {
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
    public float get(int groupIndex) {
        return getGroupedValue(groupIndex);
    }

    protected float getGroupedValue(int groupIndex) {
        return aggregateFunction.group(inputSeries, groupsStartIndexes.get(groupIndex), groupsStartIndexes.get(groupIndex + 1) - groupsStartIndexes.get(groupIndex));
    }

}
