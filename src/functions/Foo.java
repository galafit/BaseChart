package main.chart.functions;

/**
 * Created by hdablin on 23.06.17.
 */
public class Foo implements Function2D {
    @Override
    public double apply(double value) {
        if (value < 1.5) {return 4;}
        if (value > 6 && value < 6.5) {return 8;}
        if (value > 8) {return Math.log(value);}
        return 0;
    }
}
