package data;

/**
 * Created by galafit on 18/9/17.
 */
public class RegularSet implements NumberSet {
    private double startValue;
    private double dataInterval;

    public RegularSet(double startValue, double dataInterval) {
        this.startValue = startValue;
        this.dataInterval = dataInterval;
    }

    public RegularSet() {
        this(0, 1);
    }

    @Override
    public double get(int index) {
        return startValue + dataInterval * index;
    }

    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }


}
