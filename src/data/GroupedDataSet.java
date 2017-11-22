package data;


import base.Range;
import data.series.IntSeries;

/**
 * Created by galafit on 13/10/17.
 */
public class GroupedDataSet extends BaseDataSet {
    BaseDataSet dataSet;

    public GroupedDataSet(BaseDataSet dataSet, int compression) {
        super(dataSet);
        this.dataSet = dataSet;
        if(compression > 1 && (xColumn instanceof RegularColumn)) {
            for (NumberColumn numberColumn : yColumns) {
                numberColumn.groupByNumber(compression);
            }
            xColumn.groupByNumber(compression);
        }

        // interval grouping:
        if(compression > 1 && ! (xColumn instanceof RegularColumn)) {
            Range xRange = dataSet.getXExtremes();
            double groupingInterval = (xRange.end() - xRange.start()) * compression / (dataSet.size() -1);
            IntSeries groupsStartIndexis = xColumn.groupByInterval(groupingInterval);
            for (NumberColumn numberColumn : yColumns) {
                numberColumn.groupCustom(groupsStartIndexis);
            }
        }

    }

}
