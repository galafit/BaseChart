package data.datasets;

import data.DoubleSeries;
import data.Processing;
import data.Range;

/**
 * Created by galafit on 27/9/17.
 */
class DoubleColumn implements NumberColumn {
    DoubleSeries series;
    String name;

    public DoubleColumn(DoubleSeries series) {
        this.series = series;
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
        return series.get(index);
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
