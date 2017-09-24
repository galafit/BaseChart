package data_old;

import data.Range;
import grouping.Average;

/**
 * Created by galafit on 20/7/17.
 */
public class DataProcessorForNumbers extends DataProcessor<Number> {
    {
        ExtremesFunction<Number> extremesFunction = new ExtremesFunction<Number>() {
            @Override
            public Range getExtremes(Number value) {
                return new Range(value.doubleValue(), value.doubleValue());
            }
        };
        setExtremesFunction(extremesFunction);
        setGroupingFunction(new Average());
    }
}
