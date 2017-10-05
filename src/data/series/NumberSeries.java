package data.series;

/**
 * Created by galafit on 28/9/17.
 */
public class NumberSeries {
    private SeriesType type;
    private IntSeries intSeries;
    private DoubleSeries doubleSeries;

    public NumberSeries(IntSeries intSeries) {
        this.intSeries = intSeries;
    }

    public NumberSeries(DoubleSeries doubleSeries) {
        this.doubleSeries = doubleSeries;
    }

    public IntSeries getIntSeries() {
        return intSeries;
    }

    public DoubleSeries getDoubleSeries() {
        return doubleSeries;
    }


    enum SeriesType {
        INT,
        DOUBLE;
    }
}
