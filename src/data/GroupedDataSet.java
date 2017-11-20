package data;


import base.Range;
import data.series.IntSeries;

/**
 * Created by galafit on 13/10/17.
 */
public class GroupedDataSet extends BaseDataSet {
    BaseDataSet dataSet;
    IntSeries groupIndexes;

    public GroupedDataSet(BaseDataSet dataSet, int compression) {
        super(dataSet);
        this.dataSet = dataSet;
        if(compression > 1) {
            for (NumberColumn numberColumn : yColumns) {
                numberColumn.group(compression);
            }
            xColumn.group(compression);
           /* if(xColumn instanceof RegularColumn) {
                for (NumberColumn numberColumn : yColumns) {
                    numberColumn.group(compression);
                }
                xColumn.group(compression);
            } else {
                Range xRange = dataSet.getXExtremes();
                double groupingInterval = (xRange.end() - xRange.start()) * compression / (dataSet.size() -1);
                groupIndexes = xColumn.bin(groupingInterval);
                for (NumberColumn numberColumn : yColumns) {
                    numberColumn.group(groupIndexes);
                }
            }*/
        }
    }

}
