package data_old;

import base.Range;

/**
 * Created by galafit on 15/7/17.
 */
public class XYRegularSet<Y> implements RangableSet<Y> {
    private DataSet<Y> data;
    private double startPoint;
    private double pointInterval;

    public XYRegularSet(DataSet<Y> data, double startPoint, double pointInterval) {
        this.data = data;
        this.startPoint = startPoint;
        this.pointInterval = pointInterval;
    }

    public XYRegularSet(DataSet<Y> data) {
        this(data, 0, 1);
    }

    public double getPointInterval() {
        return pointInterval;
    }

    @Override
    public long size() {
        return data.size();
    }

    @Override
    public Double getX(long index) {
        return startPoint + index * pointInterval;
    }

    @Override
    public Y getY(long index) {
        return data.getData(index);
    }

    @Override
    public Range getIndexRange(double startXValue, double endXValue) {
        long indexStart = getIndexBefore(startXValue);
        long indexEnd = getIndexBefore(endXValue);
        if(getX(indexStart) < startXValue && indexStart < (data.size() -1)) {
            indexStart++;
        }
        if(indexStart == indexEnd) {
            return null;
        }
        return new Range(indexStart, indexEnd);
    }

    private long getIndexBefore (double xValue){
        long index =  (long)((xValue - startPoint) / pointInterval);
        index = Math.max(index, 0);
        index = Math.min(index,  data.size() -1);
        return index;
    }

}
