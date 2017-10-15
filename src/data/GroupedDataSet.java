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
        super(dataSet, 0, -1);
        this.dataSet = dataSet;
        if(compression > 1) {
            if(xColumnNumber < 0) {
                for (NumberColumn numberColumn : numberColumns) {
                    numberColumn.group(compression);
                }
                dataInterval = dataInterval * compression;
            } else {
                Range xRange = dataSet.getXExtremes();
                double groupingInterval = (xRange.end() - xRange.start()) * compression / (dataSet.size() -1);
                groupIndexes = numberColumns.get(xColumnNumber).bin(groupingInterval);
                for (int i = 0; i < numberColumns.size(); i++) {
                    if(i != xColumnNumber) {
                        numberColumns.get(i).group(groupIndexes);
                    }
                }
            }
        }
    }

}
