package data_old;

/**
 * Point is a pair (X, Y) where X is double and
 * Y any kind of object or array/list of objects
 */
public interface XYSet<Y>  {
    public long size();
    public Number getX(long index);
    public Y getY(long index);
}
