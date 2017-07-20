package graphs;

import data.ExtremesFunction;
import data.Range;

/**
 * Created by galafit on 20/7/17.
 */
public class DoubleGraph extends Graph<Double> {
    {
        ExtremesFunction<Double> extremesFunction = new ExtremesFunction<Double>() {
            @Override
            public Range getExtremes(Double value) {
                return new Range(value, value);
            }
        };
        dataProcessor.setExtremesFunction(extremesFunction);
    }
}
