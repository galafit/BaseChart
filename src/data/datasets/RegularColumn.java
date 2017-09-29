package data.datasets;


import data.Range;

/**
 * Created by galafit on 18/9/17.
 */
class RegularColumn implements NumberColumn {
    private double startValue;
    private double dataInterval;
    private String name;

    public RegularColumn(double startValue, double dataInterval) {
        this.startValue = startValue;
        this.dataInterval = dataInterval;
    }

    public RegularColumn() {
        this(0, 1);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getValue(int index) {
        return startValue + dataInterval * index;
    }

    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Range getMinMax() {
        return null;
    }

    @Override
    public int findNearest(double value) {
        return 0;
    }
}
