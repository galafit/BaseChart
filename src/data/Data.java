package data;

/**
 * Created by galafit on 19/9/17.
 */
public abstract class Data {
    NumberSet xSet = new RegularSet();
    NumberSet[] numberSets;

    public Data(int numberSetAmount) {
        numberSets = new NumberSet[numberSetAmount];
        for (NumberSet numberSet : numberSets) {
            numberSet = new NullNumberSet();
        }
    }

    void setNumberSet(int index, NumberSet numberSet){
        numberSets[index] = numberSet;
    }

    public void setXSet(NumberSet xSet) {
        this.xSet = xSet;
    }

    public int size() {
        int size = xSet.size();
        for (NumberSet numberSet : numberSets) {
            size = Math.min(size, numberSet.size());
        }
        return size;
    }

    public Range getXRange() {
        return new Range(xSet.get(0), xSet.get(size() - 1));
    }

    public abstract Range getYRange();

}
