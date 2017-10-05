package data;


/**
 * Created by galafit on 18/9/17.
 */
class RegularColumn implements NumberColumn {
    private double startValue;
    private double dataInterval;

    public RegularColumn(double startValue, double dataInterval) {
        this.startValue = startValue;
        this.dataInterval = dataInterval;
    }

    public RegularColumn() {
        this(0, 1);
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
    public Range getExtremes(int from, int length) {
        double min = startValue + from * dataInterval;
        double max = startValue + (from + length - 1) * dataInterval;
        return new Range(min, max);

    }

    @Override
    public int findNearest(double value, int from, int length) {
        int nearest = (int) Math.round((value - startValue) / dataInterval);
        if(nearest < from) {
            nearest = from;
        }
        if(nearest >= from + length) {
            nearest = from + length - 1;
        }
        return nearest;
    }
}
