package data;

import base.Range;

/**
 * Created by galafit on 17/9/17.
 */
public interface NumberColumn {
    public int size();
    public double getValue(int index);

    public Range getExtremes(int from, int length);
    public int upperBound(double value, int from, int length);
    public int lowerBound(double value, int from, int length);
    public NumberColumn copy();
    public void setGroupingType(GroupingType groupingType);
    public void groupByNumber(int numberOfElementsInGroups, boolean isCachingEnable);

    // at the moment "grouping by equal interval" is not used. But that is draft realisation
    // just for the case we will need it in the future

   // public IntSeries groupByInterval(float groupsInterval);
    // public void groupCustom(IntSeries groupsStartIndexes);
}

