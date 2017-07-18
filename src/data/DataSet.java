package data;

/**
 * Created by galafit on 15/7/17.
 */
public interface DataSet<T> {
    public long size();
    public T getData(long index);
}
