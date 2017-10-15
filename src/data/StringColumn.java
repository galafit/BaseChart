package data;

import data.series.StringSeries;

/**
 * Created by galafit on 28/9/17.
 */
class StringColumn {
    private StringSeries series;

    public StringColumn(StringSeries series) {
        this.series = series;
    }

    public int size() {
        return series.size();
    }

    public String getString(int index) {
        return series.get(index);
    }

    public StringColumn copy() {
        return new StringColumn(series);
    }
}
