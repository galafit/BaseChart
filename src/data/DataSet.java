package data;

import data.series.DoubleSeries;
import data.series.IntSeries;
import data.series.StringSeries;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 19/9/17.
 */
public class DataSet {
    boolean isOrdered = false;
    ArrayList<NumberColumn> numberColumns = new ArrayList<NumberColumn>();
    ArrayList<StringColumn> stringColumns = new ArrayList<StringColumn>();
    int xColumnNumber = - 1; // index of Column with X-data

    // for regular data when xColumn is not defined
    private double startXValue = 0;
    private double dataInterval = 1;

    // for subsets
    int startIndex = 0;
    int length = -1;

    public DataSet() {
    }

    /**
     * make a copy DataSet
     * @param dataSet
     */
    public DataSet(DataSet dataSet, int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
        dataInterval = dataSet.dataInterval;
        startXValue = dataSet.startXValue + (startIndex - dataSet.startIndex) * dataInterval;
        xColumnNumber = dataSet.xColumnNumber;
        for (NumberColumn numberColumn : dataSet.numberColumns) {
            numberColumns.add(numberColumn);
        }
        for (StringColumn stringColumn : dataSet.stringColumns) {
            stringColumns.add(stringColumn);
        }
    }

    public void setXColumn(double startXValue, double dataInterval) {
        this.startXValue = startXValue;
        this.dataInterval = dataInterval;
    }

    public void setXColumn(int xColumnNumber) {
        this.xColumnNumber = xColumnNumber;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public boolean isOrdered() {
        if(isOrdered || xColumnNumber < 0) {
            return true;
        }
        return false;
    }

    /**
     * add series as a number column
     * @param series - series of ints
     * @return - column index in the list of NumberColumns
     */
    public int addSeries(IntSeries series) {
        numberColumns.add(new IntColumn(series));
        return numberColumns.size() - 1;
    }

    public int addSeries(DoubleSeries series) {
        numberColumns.add(new DoubleColumn(series));
        return numberColumns.size() - 1;
    }


    /**
     * add series as a string column
     * @param series - series of Strings
     * @return - column index in the list of StringColumns
     */
    public int addSeries(StringSeries series) {
        stringColumns.add(new StringColumn(series));
        return stringColumns.size() - 1;
    }

    public void removeNumberSeries(int columnNumber) {
        numberColumns.remove(columnNumber);
    }

    public void removeStringSeries(int columnNumber) {
        stringColumns.remove(columnNumber);
    }

    public void removeXSeries() {
        if(xColumnNumber >= 0) {
            removeNumberSeries(xColumnNumber);
            xColumnNumber = - 1;
        }
    }

    public double getValue(int index, int columnNumber) {
        return numberColumns.get(columnNumber).getValue(index + startIndex);
    }

    public double getXValue(int index) {
        if(xColumnNumber >= 0) {
            return getValue(index + startIndex, xColumnNumber);
        }
        // if xColumnNumber is not specified we use indexes as xColumn values
        return startXValue + dataInterval * index;
    }

    public String getString(int index, int columnNumber) {
        return stringColumns.get(columnNumber).getString(index + startIndex);
    }

    public int size() {
        if(length < 0) {
          return fullSize();
        }
        return length;

    }

    private int fullSize() {
        int size = 0;
        if(numberColumns.size() > 0) {
            size = numberColumns.get(0).size();
        } else if (stringColumns.size() > 0) {
            size = stringColumns.get(0).size();
        }
        for (NumberColumn column : numberColumns) {
            size = Math.min(size, column.size());
        }
        for (StringColumn column : stringColumns) {
            size = Math.min(size, column.size());
        }
        return size;
    }

    public Range getXExtremes() {
        if(size() == 0) {
            return null;
        }
        if(xColumnNumber >= 0) {
            return numberColumns.get(xColumnNumber).getExtremes(startIndex, size());
        }
        // if xColumnNumber is not specified we use indexes as xColumn values
        double min = startXValue;
        double max = startXValue + (size() - 1) * dataInterval;
        return new Range(min, max);
    }

    /**
     *
     * @param xValue
     * @return index of nearest data item
     */
    public int findNearestData(double xValue) {
        if(xColumnNumber >= 0) {
            int lowerBoundIndex = numberColumns.get(xColumnNumber).lowerBound(xValue, startIndex, size());
            if (lowerBoundIndex < startIndex) {
                return startIndex;
            }
            if (lowerBoundIndex == startIndex + size() - 1) {
                return lowerBoundIndex;
            }
            double distance1 = xValue - getXValue(lowerBoundIndex);
            double distance2 = getXValue(lowerBoundIndex + 1) - xValue;
            int nearestIndex = (distance1 < distance2) ? lowerBoundIndex : lowerBoundIndex + 1;
            return nearestIndex - startIndex;
        }
        // if xColumnNumber is not specified we use indexes as xColumn values
        int nearest = (int) Math.round((xValue - startXValue) / dataInterval);
        if(nearest < 0) {
            nearest = 0;
        }
        if(nearest >= size()) {
            nearest = size() - 1;
        }
        return nearest;
    }

    public Range getExtremes(int columnNumber) {
        if(size() == 0) {
            return null;
        }
        return numberColumns.get(columnNumber).getExtremes(startIndex,  size());
    }

    public DataSet getSubset(double startXValue, double endXValue, int shoulder) {
        if(endXValue < startXValue) {
            String errorMessage = "Error during creating Data subset.Expected StartValue <= EndValue. StartValue = {0}, EndValue = {1}.";
            String formattedError = MessageFormat.format(errorMessage, startXValue, endXValue);
            throw new IllegalArgumentException(formattedError);
        }
        if(!isOrdered()) {
            return this;
        }
        int subsetStartIndex;
        int subsetEndIndex;
        if(xColumnNumber >= 0) {
            subsetStartIndex = numberColumns.get(xColumnNumber).lowerBound(startXValue, 0, fullSize());
            subsetEndIndex = numberColumns.get(xColumnNumber).upperBound(endXValue, 0, fullSize());
         } else {
            // if xColumnNumber is not specified we use indexes as xColumn values
            subsetStartIndex = (int) ((startXValue - this.startXValue) / dataInterval);
            subsetEndIndex = (int) ((endXValue - this.startXValue) / dataInterval);
            if(endXValue > getXValue(subsetEndIndex)) {
                subsetEndIndex++;
            }
            // recalculate indexes from 0
            subsetStartIndex += startIndex;
            subsetEndIndex += startIndex;
        }
        if(subsetStartIndex >= fullSize() || subsetEndIndex < 0) {
            return new DataSet(this, 0, 0);
        }
        if(subsetStartIndex - shoulder < 0){
            subsetStartIndex = 0 + shoulder;
        }
        if(subsetEndIndex + shoulder >= fullSize()) {
            subsetEndIndex = fullSize() - 1 - shoulder;
        }
        return new DataSet(this, subsetStartIndex, subsetEndIndex - subsetStartIndex + 1);
    }

    /**********************************************************************
     *     Helper Methods to add data
     *********************************************************************/

    public int addSeries(int[] data) {
       IntSeries series = new IntSeries() {
           @Override
           public int size() {
               return data.length;
           }

           @Override
           public int get(int index) {
               return data[index];
           }
       };
       return addSeries(series);
    }

    public int addSeries(double[] data) {
        DoubleSeries series = new DoubleSeries() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public double get(int index) {
                return data[index];
            }
        };
        return addSeries(series);
    }

    public int addSeries(List<? extends Number> data) {
        if(data.size() > 0 && data.get(0) instanceof Integer) {
            IntSeries series = new IntSeries() {
                @Override
                public int size() {
                    return data.size();
                }

                @Override
                public int get(int index) {
                    return data.get(index).intValue();
                }
            };
            return addSeries(series);
        } else {
            DoubleSeries series = new DoubleSeries() {
                @Override
                public int size() {
                    return data.size();
                }

                @Override
                public double get(int index) {
                    return data.get(index).doubleValue();
                }
            };
            return addSeries(series);
        }
    }
}
