package com.biorecorder.basechart.data.grouping;

import com.biorecorder.basechart.data.FloatSeries;
import com.biorecorder.basechart.data.LongSeries;
import com.biorecorder.basechart.data.grouping.aggregation.FloatAggregateFunction;

import java.text.MessageFormat;

/**
 * This class groups com.biorecorder.basechart.data in such a way that each group
 * has equal number of elements or com.biorecorder.basechart.data points.
 * Equal frequencies [equal height binning, quantiles] grouping.
 */
public class GroupedByNumberFloatSeries implements FloatSeries {
    private FloatSeries inputSeries;
    private LongSeries groupsStartIndexes;
    private FloatAggregateFunction aggregateFunction;

    public GroupedByNumberFloatSeries(FloatSeries inputSeries, int numberOfPointsInEachGroup, FloatAggregateFunction aggregateFunction) {
        this.inputSeries = inputSeries;
        this.aggregateFunction = aggregateFunction;

        groupsStartIndexes = new LongSeries() {
            @Override
            public long size() {
                return inputSeries.size() / numberOfPointsInEachGroup;
            }

            @Override
            public long get(long index) {
                return index * numberOfPointsInEachGroup;
            }
        };
    }

    /**
     * Gets resultant number of groups/bins
     */
    @Override
    public long size() {
        // last point we do not count because it will change on adding com.biorecorder.basechart.data
        return groupsStartIndexes.size();
    }

    @Override
    public float get(long groupIndex) {
        return getGroupedValue(groupIndex);
    }

    protected float getGroupedValue(long groupIndex) {
        long numberOfGroupingElements = groupsStartIndexes.get(groupIndex + 1) - groupsStartIndexes.get(groupIndex);
        if(numberOfGroupingElements > Integer.MAX_VALUE) {
            String errorMessage = "Error during calculation of grouped value. Expected: numberOfGroupingElements is integer. NumberOfGroupingElements = {0}, Integer.MAX_VALUE = {1}.";
            String formattedError = MessageFormat.format(errorMessage, numberOfGroupingElements, Integer.MAX_VALUE);
            throw new RuntimeException(formattedError);
        }
        return aggregateFunction.group(inputSeries, groupsStartIndexes.get(groupIndex), (int) numberOfGroupingElements);
    }

}
