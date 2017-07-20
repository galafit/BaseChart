package data;

/**
 * Created by galafit on 18/7/17.
 */
public class DataPointSlidingSet<Y> implements SlidingSet<Y> {
    RangableSet<Y> points;
    private long rangeStartIndex;
    private long rangeLength;
    private ExtremesFunction<Y> extremesFunction;

    public DataPointSlidingSet(DataSet<Y> dataSet) {
        points = new DataPointRegularSet<Y>(dataSet);
    }

    public DataPointSlidingSet(DataPointList<Y> dataPointList) {
        points = new DataPointOrderedSet<>(dataPointList);
    }


    @Override
    public void setXRange(double startXValue, double endXValue) {
        if(points == null) {
            return;
        }
        Range rangeIndexes = points.getIndexRange(startXValue, endXValue);
        if(rangeIndexes != null) {
            rangeStartIndex = (long) rangeIndexes.getStart();
            rangeLength = (long)rangeIndexes.getEnd() - rangeStartIndex + 1;
        }
    }

    public Range getFullXRange() {
        if(points == null) {
            return null;
        }
        return new Range(points.getX(0), points.getX(points.size() - 1));
    }

    public long getFullDataSize() {
        if(points == null) {
            return 0;
        }
        return points.size();
    }

    public Range getYRange()  {
        if(rangeLength <= 0 || points == null || points.size() == 0) {
            return null;
        }
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        for (long i = rangeStartIndex; i < rangeStartIndex + rangeLength ; i++) {
            Range extremes = extremesFunction.getExtremes(points.getY(i));
            max = Math.max(max, extremes.getEnd());
            min = Math.min(min, extremes.getStart());
        }
        return new Range(min, max);
    }


    @Override
    public long size() {
        return rangeLength;
    }

    @Override
    public Double getX(long index) {
        if(points == null) {
            return null;
        }
        return points.getX(rangeStartIndex + index);
    }

    @Override
    public Y getY(long index) {
        if(points == null) {
            return null;
        }
        return points.getY(rangeStartIndex + index);
    }
}
