package main.chart.functions;

/**
 * Created by hdablin on 20.06.17.
 */
public class Tg implements Function2D {
    @Override
    public double apply(double value) {
        return Math.tan(value);
    }
}
