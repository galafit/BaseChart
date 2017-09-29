package data.datasets;

import data.StringSeries;

/**
 * Created by galafit on 28/9/17.
 */
class StringColumn {
    private String name;
    private StringSeries series;

    public StringColumn(StringSeries series) {
        this.series = series;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int size() {
        return series.size();
    }

    public String getString(int index) {
        return series.get(index);
    }
}
