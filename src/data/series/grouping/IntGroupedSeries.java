package data.series.grouping;

import data.series.IntSeries;

/**
 * Created by galafit on 12/10/17.
 */
public class IntGroupedSeries implements IntSeries{
    private IntSeries series;
    private IntGroupingFunction groupingFunction;
    private IntSeries groupIndexes;

    public IntGroupedSeries(IntSeries series, IntGroupingFunction groupingFunction, int compression) {
        this.groupingFunction = groupingFunction;
        this.series = series;
        groupIndexes = new IntSeries() {
            @Override
            public int size() {
                return series.size() / compression + 1;
            }

            @Override
            public int get(int index) {
                return index * compression;
            }
        };
     }

    public IntGroupedSeries(IntSeries series, IntGroupingFunction groupingFunction, IntSeries groupIndexes) {
        this.groupingFunction = groupingFunction;
        this.groupIndexes = groupIndexes;
        this.series = series;
    }

    @Override
    public int size() {
        return groupIndexes.size() - 1;
    }

    @Override
    public int get(int index) {
        return groupingFunction.group(series, groupIndexes.get(index), groupIndexes.get(index + 1) - groupIndexes.get(index));
    }
}
