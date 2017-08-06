package grouping;

import java.util.List;

/**
 * Created by hdablin on 07.07.17.
 */
public class Average implements GroupingFunction<Number> {
    @Override
    public Double group(List<Number> data) {
        double sum = 0;
        for (Number value : data) {
            sum += value.doubleValue();
        }
        return sum / data.size();
    }
}
