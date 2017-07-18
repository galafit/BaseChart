package data;

/**
 * Point is a pair (X, Y) where X is double and
 * Y any kind of object or array/list of objects
 */
public interface PointsProvider<Y>  {
    public long size();
    public Double getX(long index);
    public Y getY(long index);
}
