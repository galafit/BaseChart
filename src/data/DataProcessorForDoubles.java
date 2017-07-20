package data;

import grouping.Average;

/**
 * Created by galafit on 20/7/17.
 */
public class DataProcessorForDoubles extends DataProcessor<Double> {
    {
        ExtremesFunction<Double> extremesFunction = new ExtremesFunction<Double>() {
            @Override
            public Range getExtremes(Double value) {
                return new Range(value, value);
            }
        };
        setExtremesFunction(extremesFunction);
        setGroupingFunction(new Average());
    }
}
