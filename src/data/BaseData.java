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
public class BaseData {
    boolean isOrdered = true;
    ArrayList<NumberColumn> yColumns = new ArrayList<NumberColumn>();
    NumberColumn xColumn = new RegularColumn();
    StringColumn annotationColumn;

    // for subsets
    long startIndex = 0;
    long length = -1;

    public BaseData() {
    }

    public BaseData(BaseData dataSet) {
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



    public int getYColumnsCount() {
        return yColumns.size();
    }


    public double getYValue(long index, int columnNumber) {
        return yColumns.get(columnNumber).getValue(index + startIndex);
    }

    public double getXValue(long index) {
        return xColumn.getValue(index + startIndex);
    }

    public String getAnnotation(long index) {
        if (annotationColumn != null && index < annotationColumn.size()) {
            return annotationColumn.getString(index);
        }
        return null;
    }

    public Range getXExtremes() {
        if (size() == 0) {
            return null;
        }
        if (isOrdered()) {
            double min = xColumn.getValue(startIndex);
            double max = xColumn.getValue(startIndex + size() - 1);
            return new Range(min, max);
        } else {
            long size = size();
            if (size > Integer.MAX_VALUE) {
                String errorMessage = "Error. Size must be integer during calculating X extremes for non ordered series. Size = {0}, Integer.MAX_VALUE = {1}.";
                String formattedError = MessageFormat.format(errorMessage, size, Integer.MAX_VALUE);
                throw new IllegalArgumentException(formattedError);
            }
            return xColumn.getExtremes(startIndex, (int) size);
        }
    }

    public Range getYExtremes(int yColumnNumber) {
        if (size() == 0) {
            return null;
        }
        long size = size();
        if (size > Integer.MAX_VALUE) {
            String errorMessage = "Error. Size must be integer during calculating Y extremes. Size = {0}, Integer.MAX_VALUE = {1}.";
            String formattedError = MessageFormat.format(errorMessage, size, Integer.MAX_VALUE);
            throw new IllegalArgumentException(formattedError);
        }
        return yColumns.get(yColumnNumber).getExtremes(startIndex, (int) size);
    }


    public long size() {
        if (length < 0) {
            return fullSize();
        }
        return length;

    }

    private long fullSize() {
        if (yColumns.size() == 0 && annotationColumn == null) {
            return 0;
        }
        long size = xColumn.size();
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
    public long findNearestData(double xValue) {
        long size = size();
        if (size > Integer.MAX_VALUE) {
            String errorMessage = "Error. Size must be integer during finding nearest data. Size = {0}, Integer.MAX_VALUE = {1}.";
            String formattedError = MessageFormat.format(errorMessage, size, Integer.MAX_VALUE);
            throw new IllegalArgumentException(formattedError);
        }
        long lowerBoundIndex = xColumn.lowerBound(xValue, startIndex, (int) size);
        lowerBoundIndex -= startIndex;
        if (lowerBoundIndex < 0) {
            return 0;
        }
        if (lowerBoundIndex >= size() - 1) {
            return size() - 1;
        }
        double distance1 = xValue - getXValue(lowerBoundIndex);
        double distance2 = getXValue(lowerBoundIndex + 1) - xValue;
        long nearestIndex = (distance1 <= distance2) ? lowerBoundIndex : lowerBoundIndex + 1;
        return nearestIndex;
    }

    public DataSet getDataSet() {
        BaseData subset = new BaseData(this);
        subset.startIndex = startIndex;
        subset.length = size();
        if(subset.length > Integer.MAX_VALUE) {
            String errorMessage = "Error during creating DataSize. Resultant size must be integer. Resultant size = {0}, Integer.MAX_VALUE = {1}.";
            String formattedError = MessageFormat.format(errorMessage, subset.length, Integer.MAX_VALUE);
            throw new RuntimeException(formattedError);
        }
        return new DataSet() {
            @Override
            public int getYColumnsCount() {
                return subset.getYColumnsCount();
            }

            @Override
            public double getYValue(int index, int yColumnNumber) {
                return subset.getYValue(index, yColumnNumber);
            }

            @Override
            public double getXValue(int index) {
                return subset.getXValue(index);
            }

            @Override
            public String getAnnotation(int index) {
                return subset.getAnnotation(index);
            }

            @Override
            public int size() {
                return (int) subset.size();
            }

            @Override
            public Range getXExtremes() {
                return subset.getXExtremes();
            }

            @Override
            public Range getYExtremes(int yColumnNumber) {
                return subset.getYExtremes(yColumnNumber);
            }

            @Override
            public int findNearestData(double xValue) {
                return (int)subset.findNearestData(xValue);
            }

            @Override
            public double getAverageDataInterval() {
                return subset.getAverageDataInterval();
            }
        };
    }

    public BaseData getSubset(double startXValue, double endXValue, int shoulder) {
        if (endXValue < startXValue) {
            String errorMessage = "Error during creating subset. Expected StartValue <= EndValue. StartValue = {0}, EndValue = {1}.";
            String formattedError = MessageFormat.format(errorMessage, startXValue, endXValue);
            throw new IllegalArgumentException(formattedError);
        }
        long fullSize = fullSize();
        if (!(xColumn instanceof RegularColumn) && fullSize > Integer.MAX_VALUE) {
            String errorMessage = "Error during creating subset. Full size must be integer for no regular data sets. Full size = {0}, Integer.MAX_VALUE = {1}.";
            String formattedError = MessageFormat.format(errorMessage, fullSize, Integer.MAX_VALUE);
            throw new RuntimeException(formattedError);
        }

        long subsetLength = size();
        long subsetStartIndex = 0;
        if (isOrdered()) {
            subsetStartIndex = xColumn.lowerBound(startXValue, 0, (int)fullSize);
            long subsetEndIndex = xColumn.upperBound(endXValue, 0, (int)fullSize);
            subsetStartIndex -= shoulder;
            subsetEndIndex += shoulder;

            if (subsetStartIndex < 0) {
                subsetStartIndex = 0;
            }
            if (subsetEndIndex >= fullSize()) {
                subsetEndIndex = fullSize() - 1;
            }
            subsetLength = subsetEndIndex - subsetStartIndex + 1;
            if (subsetStartIndex >= fullSize() || subsetEndIndex < 0) {
                subsetLength = 0;
            }
        }
        BaseData subset = new BaseData(this);
        subset.startIndex = subsetStartIndex;
        subset.length = subsetLength;
       return subset;
    }

    public BaseData getSubset(double startXValue, double endXValue) {
        return getSubset(startXValue, endXValue, 1);
    }

        /**
         * Grouping by equal number of items in each group
         *
         * @param numberOfItemsInGroups
         * @return DataSet with grouped data
         */
    public BaseData groupByNumber(int numberOfItemsInGroups, boolean isCachingEnable) {
        BaseData groupedSet = new BaseData(this);
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
    public BaseData groupByInterval(double groupingInterval, boolean isCachingEnable) {
        // at the moment "grouping by equal interval" is not fully realized.
        // That is draft realisation
        // just for the case we will need it in the future
       /* if (!(xColumn instanceof RegularColumn)) {
            BaseData groupedSet = new BaseData(this);
            IntSeries groupsStartIndexes = groupedSet.xColumn.groupByInterval(groupingInterval);
            for (NumberColumn numberColumn : groupedSet.yColumns) {
                numberColumn.groupCustom(groupsStartIndexes);
            }
            return groupedSet;
        }*/

        double avgDataInterval = getAverageDataInterval();
        if (avgDataInterval > 0) {
            int avgNumberOfItemsInGroups = (int) Math.round(groupingInterval / getAverageDataInterval());
            return groupByNumber(avgNumberOfItemsInGroups, isCachingEnable);
        }
        return new BaseData(this);
    }


    public double getAverageDataInterval() {
        if (xColumn instanceof RegularColumn) {
            return ((RegularColumn) xColumn).getDataInterval();
        }
        if (size() > 1) {
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
