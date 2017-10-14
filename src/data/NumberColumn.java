package data;

import base.Range;
import data.series.IntSeries;

/**
 * Created by galafit on 17/9/17.
 */
public interface NumberColumn {
    public int size();
    public double getValue(int index);

    public Range getExtremes(int from, int length);
    public int upperBound(double value, int from, int length);
    public int lowerBound(double value, int from, int length);
    public void group(int groupInterval);
    public void group(IntSeries groupIndexes);
    public IntSeries bin(double binInterval);
}

