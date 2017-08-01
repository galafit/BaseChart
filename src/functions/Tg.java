package functions;


/**
 * Created by hdablin on 20.06.17.
 */
public class Tg implements DoubleFunction<Double> {
    @Override
    public Double apply(double value) {
        return Math.tan(value);
    }
}
