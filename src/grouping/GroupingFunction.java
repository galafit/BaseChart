package grouping;

import java.util.List;

/**
 * Created by hdablin on 07.07.17.
 */
public interface GroupingFunction<T> {
    public T group(List<T> data);
}

