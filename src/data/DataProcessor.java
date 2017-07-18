package data;




/**
 * Created by galafit on 15/7/17.
 */
public class DataProcessor<Y> implements PointsProvider<Y> {
    private RangeblePoints<Y> points;
    private long rangeStartIndex;
    private long rangeLength;
    private ExtremesFunction<Y> extremesFunction;
    private GroupedPoints<Y> groupedPoints;
    private int maxVisiblePoint = 500;

    public DataProcessor() {
    }

    public DataProcessor(DataSet<Y> dataSet) {
        points = new RegularPoints<Y>(dataSet);
    }

    public DataProcessor(PointsList<Y> pointsList) {
        points = new OrderedPoints<>(pointsList);
    }

    public void setData(DataSet<Y> dataSet) {
        points = new RegularPoints<Y>(dataSet);
    }

    public void setData(PointsList<Y> pointsList) {
        points = new OrderedPoints<>(pointsList);
    }

    public void setExtremesFunction(ExtremesFunction<Y> extremesFunction) {
        this.extremesFunction = extremesFunction;
    }

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

    private double calculateGroupingInterval(double startXValue, double endXValue) {
        //double min = points.getX(rangeStartIndex);
        //double max = points.getX(rangeStartIndex + rangeLength - 1);
        return (endXValue - startXValue) / maxVisiblePoint;

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

    public Range getFullXRange() {
       if(points == null) {
           return null;
       }
        return new Range(points.getX(0), points.getX(points.size() - 1));
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
