package data.datasets;

import data.DoubleSeries;
import data.IntSeries;
import data.Range;
import data.StringSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 19/9/17.
 */
public class DataSet {
    private ArrayList<NumberColumn> numberColumns = new ArrayList<NumberColumn>();
    private ArrayList<StringColumn> stringColumns = new ArrayList<StringColumn>();
    private int xColumnNumber = - 1; // index of Column with X-data

    public void setXColumn(int xColumnNumber) {
        this.xColumnNumber = xColumnNumber;
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

    public int addSeries(double startValue, double dataInterval) {
        numberColumns.add(new RegularColumn(startValue, dataInterval));
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
        return numberColumns.get(columnNumber).getValue(index);
    }

    public double getXValue(int index) {
        if(xColumnNumber >= 0) {
            return getValue(index, xColumnNumber);
        }
        return index;
    }

    public String getString(int index, int columnNumber) {
        return stringColumns.get(columnNumber).getString(index);
    }

    public int size() {
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
            return numberColumns.get(xColumnNumber).getExtremes(0, size());
        }
        // if xColumnNumber is not specified we use indexes as xColumnNumber
        return new Range(0, size() - 1);
    }

    public int getNearestX(double xValue) {
        if(xColumnNumber >= 0) {
            return numberColumns.get(xColumnNumber).findNearest(xValue, 0, size());
        }
        // if xColumnNumber is not specified we use indexes as xColumnNumber
        int nearest = (int)Math.round(xValue);
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
        return numberColumns.get(columnNumber).getExtremes(0, size());
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
