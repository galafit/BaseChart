package data;

import base.Range;
import data.series.IntSeries;

/**
 * Created by galafit on 1/11/17.
 */
public class RegularColumn implements NumberColumn{
    double startValue;
    double dataInterval;

    public RegularColumn(double startValue, double dataInterval) {
        this.startValue = startValue;
        this.dataInterval = dataInterval;
    }

    public RegularColumn() {
        this(0, 1);
    }

    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public double getValue(int index) {
        return startValue + dataInterval * index;
    }

    @Override
    public Range getExtremes(int from, int length) {
        double min = startValue;
        double max = startValue + (size() - 1) * dataInterval;
        return new Range(min, max);
    }

    @Override
    public int upperBound(double value, int from, int length) {
        int lowerBound = lowerBound(value, from, length);
        if(value == getValue(lowerBound)) {
            return lowerBound;
        }
        return lowerBound + 1;
    }

    @Override
    public int lowerBound(double value, int from, int length) {
        return (int) ((value - startValue) / dataInterval);
    }

    @Override
    public void groupByNumber(int numberOfElementsInGroup) {
        dataInterval = dataInterval * numberOfElementsInGroup;
    }

    @Override
    public IntSeries groupByInterval(double groupsInterval) {
        return null;
    }

    @Override
    public void groupCustom(IntSeries groupsStartIndexes) {

    }

    @Override
    public NumberColumn copy() {
        return new RegularColumn(startValue, dataInterval);
    }
}
