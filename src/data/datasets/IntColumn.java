package data.datasets;

import data.IntSeries;
import data.Processing;
import data.Range;
import java.util.function.IntToDoubleFunction;

/**
 * Created by galafit on 27/9/17.
 */
class IntColumn implements NumberColumn {
    IntSeries series;
    String name;
    IntToDoubleFunction intToDoubleFunction;

    public IntColumn(IntSeries series) {
        this.series = series;
    }

    public void setIntToDoubleFunction(IntToDoubleFunction intToDoubleFunction) {
        this.intToDoubleFunction = intToDoubleFunction;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int size() {
        return series.size();
    }

    @Override
    public double getValue(int index) {
        if(intToDoubleFunction != null) {
            return series.get(index);
        }
        return intToDoubleFunction.applyAsDouble(series.get(index));
    }

    @Override
    public Range getMinMax() {
        return Processing.minMaxRange(series);
    }

    @Override
    public int findNearest(double value) {
        int lowerBoundIndex = Processing.lowerBound(series, 0, series.size(), value);
        if (lowerBoundIndex < 0) {
            return 0;
        }
        if (lowerBoundIndex == series.size() - 1) {
            return lowerBoundIndex;
        }
        double distance1 = value - series.get(lowerBoundIndex);
        double distance2 = series.get(lowerBoundIndex + 1) - value;
        int nearestIndex = (distance1 < distance2) ? lowerBoundIndex : lowerBoundIndex + 1;
        return nearestIndex;
    }
}
