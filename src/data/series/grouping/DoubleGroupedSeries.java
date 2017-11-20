package data.series.grouping;

import data.series.DoubleSeries;
import data.series.IntSeries;
import data.series.grouping.aggregation.DoubleAggregateFunction;

/**
 * Created by galafit on 14/10/17.
 */
public class DoubleGroupedSeries implements DoubleSeries {
    private DoubleSeries series;
    private DoubleAggregateFunction groupingFunction;
    private IntSeries groupIndexes;

    public DoubleGroupedSeries(DoubleSeries series, DoubleAggregateFunction groupingFunction, int compression) {
        this.groupingFunction = groupingFunction;
        this.series = series;
        groupIndexes = new IntSeries() {
            @Override
            public int size() {
                return series.size() / compression;
            }

            @Override
            public int get(int index) {
                return index * compression;
            }
        };
    }

    public DoubleGroupedSeries(DoubleSeries series, DoubleAggregateFunction groupingFunction, IntSeries groupIndexes) {
        this.groupingFunction = groupingFunction;
        this.groupIndexes = groupIndexes;
        this.series = series;
    }

    @Override
    public int size() {
        return groupIndexes.size() - 1;
    }

    @Override
    public double get(int index) {
        return groupingFunction.group(series, groupIndexes.get(index), groupIndexes.get(index + 1) - groupIndexes.get(index));
    }
}
