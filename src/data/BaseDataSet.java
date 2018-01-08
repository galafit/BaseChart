package data;

import base.DataSet;
import base.Range;
import data.series.FloatSeries;
import data.series.IntSeries;
import data.series.StringSeries;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 19/9/17.
 */
public class BaseDataSet implements DataSet {
    boolean isOrdered = true;
    ArrayList<NumberColumn> yColumns = new ArrayList<NumberColumn>();
    NumberColumn xColumn = new RegularColumn();
    StringColumn annotationColumn;

    // for subsets
    int startIndex = 0;
    int length = -1;

    public BaseDataSet() {
    }

    public BaseDataSet(BaseDataSet dataSet) {
        for (NumberColumn numberColumn : dataSet.yColumns) {
            yColumns.add(numberColumn.copy());
        }
        xColumn = dataSet.xColumn.copy();
        annotationColumn = dataSet.annotationColumn;
    }

    public boolean isOrdered() {
        if (isOrdered || xColumn instanceof RegularColumn) {
            return true;
        }
        return false;
    }


    public void setYGroupingType(GroupingType groupingType, int columnNumber) {
        yColumns.get(columnNumber).setGroupingType(groupingType);
    }


    public void setXData(double startXValue, double dataInterval) {
        xColumn = new RegularColumn(startXValue, dataInterval);
    }

    public void setXData(IntSeries series) {
        xColumn = new IntColumn(series);
    }

    public void setXData(FloatSeries series) {
        xColumn = new FloatColumn(series);
    }

    public void addYData(IntSeries series) {
        yColumns.add(new IntColumn(series));
    }

    public void addYData(FloatSeries series) {
        yColumns.add(new FloatColumn(series));
    }

    public void setAnnotations(StringSeries series) {
        annotationColumn = new StringColumn(series);
    }

    public void removeYData(int columnNumber) {
        yColumns.remove(columnNumber);
    }


    @Override
    public int getYColumnsCounter() {
        return yColumns.size();
    }


    @Override
    public double getYValue(int index, int columnNumber) {
        return yColumns.get(columnNumber).getValue(index + startIndex);
    }

    @Override
    public double getXValue(int index) {
        return xColumn.getValue(index + startIndex);
    }

    @Override
    public String getAnnotation(int index) {
        if (annotationColumn != null && index < annotationColumn.size()) {
            return annotationColumn.getString(index);
        }
        return null;
    }

    @Override
    public Range getXExtremes() {
        if (size() == 0) {
            return null;
        }
        if (isOrdered()) {
            double min = xColumn.getValue(startIndex);
            double max = xColumn.getValue(startIndex + size() - 1);
            return new Range(min, max);
        } else {
            return xColumn.getExtremes(startIndex, size());
        }
    }

    @Override
    public Range getYExtremes(int yColumnNumber) {
        if (size() == 0) {
            return null;
        }
        return yColumns.get(yColumnNumber).getExtremes(startIndex, size());
    }


    @Override
    public int size() {
        if (length < 0) {
            return fullSize();
        }
        return length;

    }

    private int fullSize() {
        if (yColumns.size() == 0 && annotationColumn == null) {
            return 0;
        }
        int size = xColumn.size();
        for (NumberColumn column : yColumns) {
            size = Math.min(size, column.size());
        }
        if (annotationColumn != null) {
            size = Math.min(size, annotationColumn.size());
        }

        return size;
    }


    /**
     * @param xValue
     * @return index of nearest data item
     */
    @Override
    public int findNearestData(double xValue) {
        int lowerBoundIndex = xColumn.lowerBound(xValue, startIndex, size());
        lowerBoundIndex -= startIndex;
        if (lowerBoundIndex < 0) {
            return 0;
        }
        if (lowerBoundIndex >= size() - 1) {
            return size() - 1;
        }
        double distance1 = xValue - getXValue(lowerBoundIndex);
        double distance2 = getXValue(lowerBoundIndex + 1) - xValue;
        int nearestIndex = (distance1 <= distance2) ? lowerBoundIndex : lowerBoundIndex + 1;
        return nearestIndex;
    }

    public BaseDataSet getSubset(double startXValue, double endXValue) {
        return getSubset(startXValue, endXValue, 1);
    }


    public BaseDataSet getSubset(double startXValue, double endXValue, int shoulder) {
        if (endXValue < startXValue) {
            String errorMessage = "Error during creating Data subset.Expected StartValue <= EndValue. StartValue = {0}, EndValue = {1}.";
            String formattedError = MessageFormat.format(errorMessage, startXValue, endXValue);
            throw new IllegalArgumentException(formattedError);
        }
        if (!isOrdered()) {
            return this;
        }
        int subsetStartIndex = 0;
        int subsetEndIndex = fullSize() - 1;
        subsetStartIndex = xColumn.lowerBound(startXValue, 0, fullSize());
        subsetEndIndex = xColumn.upperBound(endXValue, 0, fullSize());
        subsetStartIndex -= shoulder;
        subsetEndIndex += shoulder;
        BaseDataSet subset = new BaseDataSet(this);
        if (subsetStartIndex >= fullSize() || subsetEndIndex < 0) {
            subset.length = 0;
            return subset;
        }
        if (subsetStartIndex < 0) {
            subsetStartIndex = 0;
        }
        if (subsetEndIndex >= fullSize()) {
            subsetEndIndex = fullSize() - 1;
        }
        subset.startIndex = subsetStartIndex;
        subset.length = subsetEndIndex - subsetStartIndex + 1;
        return subset;
    }

    /**
     * Grouping by equal number of items in each group
     *
     * @param numberOfItemsInGroups
     * @return DataSet with grouped data
     */
    public BaseDataSet groupByNumber(int numberOfItemsInGroups, boolean isCachingEnable) {
        BaseDataSet groupedSet = new BaseDataSet(this);
        if (numberOfItemsInGroups > 1) {
            for (NumberColumn numberColumn : groupedSet.yColumns) {
                numberColumn.groupByNumber(numberOfItemsInGroups, isCachingEnable);
            }
            groupedSet.xColumn.groupByNumber(numberOfItemsInGroups, isCachingEnable);
        }
        return groupedSet;


    }

    /**
     * At the moment not realised correctly!!! work only for regular DataSet
     * <p>
     * grouping by equal interval
     *
     * @param groupingInterval
     * @return DataSet with grouped data
     */
    public BaseDataSet groupByInterval(double groupingInterval, boolean isCachingEnable) {
        // at the moment "grouping by equal interval" is not fully realized.
        // That is draft realisation
        // just for the case we will need it in the future
       /* if (!(xColumn instanceof RegularColumn)) {
            BaseDataSet groupedSet = new BaseDataSet(this);
            IntSeries groupsStartIndexes = groupedSet.xColumn.groupByInterval(groupingInterval);
            for (NumberColumn numberColumn : groupedSet.yColumns) {
                numberColumn.groupCustom(groupsStartIndexes);
            }
            return groupedSet;
        }*/

        double avgDataInterval = getAverageDataInterval();
        if(avgDataInterval > 0) {
            int avgNumberOfItemsInGroups = (int) Math.round(groupingInterval / getAverageDataInterval());
            return groupByNumber(avgNumberOfItemsInGroups, isCachingEnable);
        }
        return new BaseDataSet(this);
    }


    public double getAverageDataInterval() {
        if(xColumn instanceof RegularColumn) {
            return ((RegularColumn)xColumn).getDataInterval();
        }
        if(size() > 1) {
            return getXExtremes().length() / (size() - 1);
        }
        return -1;
    }


    /**********************************************************************
     *     Helper Methods to add data
     *********************************************************************/

    public void setXData(int[] data) {
        xColumn = new IntColumn(data);
    }

    public void setXData(float[] data) {
        xColumn = new FloatColumn(data);
    }

    public void setXData(List<? extends Number> data) {
        if (data.size() > 0 && data.get(0) instanceof Integer) {
            xColumn = new IntColumn((List<Integer>) data);
        } else {
            xColumn = new FloatColumn((List<Float>) data);
        }
    }


    public void addYData(int[] data) {
        yColumns.add(new IntColumn(data));
    }

    public void addYData(float[] data) {
        yColumns.add(new FloatColumn(data));
    }

    public void addYData(List<? extends Number> data) {
        if (data.size() > 0 && data.get(0) instanceof Integer) {
            yColumns.add(new IntColumn((List<Integer>) data));
        } else {
            yColumns.add(new FloatColumn((List<Float>) data));
        }
    }
}
