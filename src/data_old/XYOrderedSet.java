package data_old;

import base.Range;

/**
 * Created by galafit on 17/7/17.
 */
public class XYOrderedSet<Y> implements RangableSet<Y> {
    XYSet<Y> points;

    public XYOrderedSet(XYSet<Y> points) {
        this.points = points;
    }


    public long prevOrNextXValueBinarySearch(double xValue, boolean previous) {
        if(xValue < points.getX(0).doubleValue()) {
            if(previous) {
                return -1; // key not found
            }
            return 0;
        }

        if (xValue > points.getX(points.size() - 1).doubleValue()) {
            if(previous) {
                return (size() - 1);
            }
            return -1;
        }
        int low = 0;
        int high = (int) points.size() - 1;
        int index = -1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double midVal = points.getX(mid).doubleValue();

            if (midVal < xValue)
                low = mid + 1;  // Neither val is NaN, thisVal is smaller
            else if (midVal > xValue)
                high = mid - 1; // Neither val is NaN, thisVal is larger
            else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(xValue);
                if (midBits == keyBits) { // Values are equal
                    index = mid;
                    if (previous)
                        high = mid - 1;
                    else
                        low = low + 1;
                } else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                else                        // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
            }
        }
        if (index < 0) {
            if (previous)
                return high;
            else
                return low;
        }
        return index;
    }

    public long getNearestPoint(double xValue) {
        if (size() == 0) {
            return -1;
        }
        if (size() == 1) {
            return 0;
        }
        long prevIndex = prevOrNextXValueBinarySearch(xValue, true);

        if (prevIndex < 0) { // means that all elements are bigger then xValue
            return 0;
        }else {
            if(prevIndex == size() - 1) {
              return size() - 1;
            } else {
                double distanceToPrev = Math.abs(xValue - points.getX(prevIndex).doubleValue());
                double distanceToNext = Math.abs(xValue - points.getX(prevIndex + 1).doubleValue());
                long nearestIndex = (distanceToPrev <= distanceToNext)? prevIndex : prevIndex + 1;
                return nearestIndex;
            }

        }
    }


    @Override
    public Range getIndexRange(double startXValue, double endXValue) {
        if (endXValue < points.getX(0).doubleValue() || startXValue > points.getX(points.size() - 1).doubleValue()) {
            return null;
        }
        long startIndex = prevOrNextXValueBinarySearch(startXValue, false);
        long endIndex = prevOrNextXValueBinarySearch(endXValue, true);
        return new Range(startIndex, endIndex);
    }

    @Override
    public long size() {
        return points.size();
    }

    @Override
    public Number getX(long index) {
        return points.getX(index);
    }

    @Override
    public Y getY(long index) {
        return points.getY(index);
    }
}
