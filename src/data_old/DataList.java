package data_old;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by galafit on 15/7/17.
 */
public class DataList<T> implements DataSet<T> {
    private ArrayList<T> list = new ArrayList<T>();

    @Override
    public long size() {
        return list.size();
    }

    @Override
    public T getData(long index) {
        if(index > Integer.MAX_VALUE) {
            String errorMessage = "Illegal index. Index = {0}.  Expected <= {1}";
            String formattedError = MessageFormat.format(errorMessage,index, Integer.MAX_VALUE);
            throw new IllegalArgumentException(formattedError);
        }
        return list.get((int)index);
    }

    public void addData(T value) {
        list.add(value);
    }
}
