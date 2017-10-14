package data.series.grouping;

import data.series.DoubleSeries;
import data.series.IntArrayList;
import data.series.IntSeries;

/**
 * Created by galafit on 14/10/17.
 */
public class DoubleBinnedSeries implements DoubleSeries {
    private DoubleSeries series;
    private IntArrayList binIndexes = new IntArrayList();
    private double binInterval; // value interval

    public DoubleBinnedSeries(DoubleSeries series, double binInterval) {
        this.series = series;
        this.binInterval = binInterval;
    }

    public IntSeries bin(int length) {
        if(series.size() == 0) {
            return binIndexes;
        }
        // remove last bin because new data can belong to that bin
        if (binIndexes.size() > 1) {
            binIndexes.remove(binIndexes.size() - 1);
        }
        // first index always 0;
        if (binIndexes.size() == 0) {
            binIndexes.add(0);
        }
        int binIndex = binIndexes.get(binIndexes.size() - 1);
        double binValue = getBinValue(binIndexes.get(binIndexes.size() - 1));
        for (int i = binIndex; i < length; i++) {
            if (series.get(i) >= binValue + binInterval) {
                binIndexes.add(i);
                binValue += binInterval;
                if (series.get(i) > binValue) {
                    binValue = getBinValue(i);
                }
            }
        }
        // close last bin
        binIndexes.add(size() - 1);
        return binIndexes;
    }

    public IntSeries bin() {
        return bin(series.size());
    }

    private double getBinValue(int seriesIndex) {
        return ((int)(series.get(seriesIndex) / binInterval)) * binInterval;
    }

    @Override
    public int size() {
        return binIndexes.size() - 1;
    }

    @Override
    public double get(int index) {
        return getBinValue(binIndexes.get(index));
    }
}
