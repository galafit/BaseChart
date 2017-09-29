package data.datasets;

import data.DoubleSeries;
import data.IntSeries;
import data.Range;
import data.StringSeries;

import java.util.ArrayList;

/**
 * Created by galafit on 19/9/17.
 */
public abstract class DataSet {
    private ArrayList<NumberColumn> numberColumns = new ArrayList<NumberColumn>();
    private ArrayList<StringColumn> stringColumns = new ArrayList<StringColumn>();

    public DataSet() {
        numberColumns.add(new RegularColumn());
    }

    public void setSeries(int columnNumber, IntSeries series) {
        setColumn(columnNumber, new IntColumn(series));
    }

    public void setSeries(int columnNumber, double startValue, double dataInterval) {

    }

    public void setSeries(int columnNumber, DoubleSeries series) {
        setColumn(columnNumber, new DoubleColumn(series));
    }

    public void setSeries(int columnNumber, StringSeries series) {
        setColumn(columnNumber, new StringColumn(series));
    }

    public void setSeriesName(int columnNumber, String name) {
        if(columnNumber < numberColumns.size()) {
            numberColumns.get(columnNumber).setName(name);
        } else {
            stringColumns.get(columnNumber - numberColumns.size()).setName(name);
        }
    }

    public String getSeriesName(int columnNumber) {
        if(columnNumber < numberColumns.size()) {
            numberColumns.get(columnNumber).getName();
        } else {
            stringColumns.get(columnNumber - numberColumns.size()).getName();
        }
    }

    public double getValue(int index, int columnNumber) {
        return numberColumns.get(columnNumber).getValue(index);
    }

    public String getString(int index, int columnNumber) {
        return stringColumns.get(columnNumber).getString(index);
    }

    private void setColumn(int index, NumberColumn column) {
        for (int i = 0; i < index - numberColumns.size(); i++) {
            numberColumns.add(new NullColumn());
        }
        numberColumns.set(index, column);
    }

    private void setColumn(int index, StringColumn column) {
        for (int i = 0; i < index - stringColumns.size(); i++) {
            stringColumns.add(new NullColumn());
        }
        stringColumns.set(index, column);
    }

    public int size() {
        int size = 0;
        for (NumberColumn column : numberColumns) {
            size = Math.min(size, column.size());
        }
        for (StringColumn column : stringColumns) {
            size = Math.min(size, column.size());
        }

        return size;
    }

    class NullColumn extends StringColumn implements NumberColumn {
        public NullColumn() {
            super(null);
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getString(int index) {
            return null;
        }
        @Override
        public int size() {
            return 0;
        }
        @Override
        public double getValue(int index) {
            return 0;
        }

        @Override
        public Range getMinMax() {
            return null;
        }

        @Override
        public int findNearest(double value) {
            return -1;
        }
    }
}
