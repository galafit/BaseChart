package data;

/**
 * Created by galafit on 20/9/17.
 */
public class XYData extends Data {
    public XYData() {
        super(1);
    }

    public void setYSet(NumberSet numberSet) {
        setNumberSet(0, numberSet);
    }

    @Override
    public Range getYRange() {
        return null; // SetUtils.minMaxRange(numberSets[0]);
    }

    public double getX(int index) {
        return xSet.get(index);
    }

    public double getY(int index) {
        return numberSets[0].get(index);
    }
}
