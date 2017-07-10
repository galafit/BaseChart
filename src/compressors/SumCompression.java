package main.chart.compressors;

/**
 * Created by hdablin on 07.07.17.
 */
public class SumCompression implements CompressFunction {
    @Override
    public double compress(double[] data) {
        double sum = 0;
        for (double datum : data) {
            sum += datum;
        }
        return sum / data.length;
    }
}
