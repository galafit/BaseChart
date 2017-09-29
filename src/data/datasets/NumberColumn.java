package data.datasets;

import data.Range;

/**
 * Created by galafit on 17/9/17.
 */
public interface NumberColumn {
    public String getName();
    public void setName(String name);
    public int size();
    public double getValue(int index);

    public Range getMinMax();
    public int findNearest(double value);
}

