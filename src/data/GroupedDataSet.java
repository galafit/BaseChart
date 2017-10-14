package data;


import data.series.IntSeries;

/**
 * Created by galafit on 13/10/17.
 */
public class GroupedDataSet extends BaseDataSet {
    BaseDataSet dataSet;
    IntSeries groupIndexes;

    public GroupedDataSet(BaseDataSet dataSet, double groupingInterval) {
        super(dataSet, 0, -1);
        this.dataSet = dataSet;

        if(xColumnNumber < 0) {
            int groupIndexInterval = (int)(groupingInterval / dataInterval);
            if(groupIndexInterval > 1) {
                for (NumberColumn numberColumn : numberColumns) {
                    numberColumn.group(groupIndexInterval);
                }
            }
        } else {
            groupIndexes = numberColumns.get(xColumnNumber).bin(groupingInterval);
            for (int i = 0; i < numberColumns.size(); i++) {
                if(i != xColumnNumber) {
                   numberColumns.get(i).group(groupIndexes);
                }
            }
        }
    }
}
