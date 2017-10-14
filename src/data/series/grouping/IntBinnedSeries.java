package data.series.grouping;

import data.series.IntArrayList;
import data.series.IntSeries;

/**
 * Created by galafit on 14/10/17.
 */
public class IntBinnedSeries implements IntSeries {
    private IntSeries series;
    private IntArrayList binIndexes = new IntArrayList();
    private int binInterval; // value interval

    public IntBinnedSeries(IntSeries series, int binInterval) {
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
        int binValue = getBinValue(binIndexes.get(binIndexes.size() - 1));
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

    private int getBinValue(int seriesIndex) {
        return (series.get(seriesIndex) / binInterval) * binInterval;
    }

    @Override
    public int size() {
        return binIndexes.size() - 1;
    }

    @Override
    public int get(int index) {
        return getBinValue(binIndexes.get(index));
    }

    /**
     * Test method
     */
    public static void main(String args[]) {
        IntArrayList series = new IntArrayList();
        series.add(1, 2, 3, 6, 7, 10, 14, 16, 20, 100);
        IntBinnedSeries binnedSeries = new IntBinnedSeries(series, 3);
        IntSeries binIndexes = binnedSeries.bin(3);

        System.out.println(binnedSeries.size() + " size :"+ binIndexes.size());
        for (int i = 0; i < binnedSeries.size() ; i++) {
            System.out.println("bin value: "+ binnedSeries.get(i)+ " bin start index: " + binIndexes.get(i));
        }

        binIndexes = binnedSeries.bin();
        System.out.println(binnedSeries.size() + " size :"+ binIndexes.size());
        for (int i = 0; i < binnedSeries.size() ; i++) {
            System.out.println("bin value: "+ binnedSeries.get(i)+ " bin start index: " + binIndexes.get(i));
        }
    }
}
