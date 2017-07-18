package data;

/**
 * Created by galafit on 17/7/17.
 */
public class OrderedPoints<Y> implements RangeblePoints<Y> {
    PointsProvider<Y> points;

    public OrderedPoints(PointsProvider<Y> points) {
        this.points = points;
    }

    @Override
    public Range getIndexRange(double startXValue, double endXValue) {
        if (startXValue >= points.getX(size() - 1) || endXValue <= points.getX(0)) {
            return null;
        }
        long endIndex = 0;
        long startIndex = 0;
        for (long i = 0; i < points.size(); i++) {
            startIndex = i;
            if (startXValue <= points.getX(i)) {
                break;
            }
        }
        for (long i = startIndex; i < points.size(); i++) {
            endIndex = i;
            if (endXValue <= points.getX(i)) {
                break;
            }
        }
        return new Range(startIndex, endIndex);
    }

    @Override
    public long size() {
        return points.size();
    }

    @Override
    public Double getX(long index) {
        return points.getX(index);
    }

    @Override
    public Y getY(long index) {
        return points.getY(index);
    }
}
