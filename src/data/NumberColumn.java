package data;

import data.Range;

/**
 * Created by galafit on 17/9/17.
 */
public interface NumberColumn {
    public int size();
    public double getValue(int index);

    public Range getExtremes(int from, int length);
    public int findNearest(double value, int from, int length);
}

