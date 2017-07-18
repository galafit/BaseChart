package functions;

import java.util.function.DoubleFunction;

/**
 * Created by hdablin on 23.06.17.
 */
public class Foo implements DoubleFunction<Double> {
    @Override
    public Double apply(double value) {
        if (value < 1.5) {return 4.0;}
        if (value > 6 && value < 6.5) {return 8.0;}
        if (value > 8) {return Math.log(value);}
        return 0.0;
    }
}
