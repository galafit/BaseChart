package functions;


/**
 * Created by hdablin on 19.06.17.
 */
public class Sin implements DoubleFunction<Double> {
    @Override
    public Double apply(double value) {
        return 100*Math.sin(value);
    }
}
