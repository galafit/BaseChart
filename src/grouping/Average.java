package grouping;

import java.util.List;

/**
 * Created by hdablin on 07.07.17.
 */
public class Average implements GroupingFunction<Double> {
    @Override
    public Double group(List<Double> data) {
        double sum = 0;
        for (double value : data) {
            sum += value;
        }
        return sum / data.size();
    }
}
