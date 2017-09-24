package data;

/**
 * Created by galafit on 20/9/17.
 */
public class NullNumberSet implements NumberSet {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public double get(int index) {
        return 0;
    }
}
