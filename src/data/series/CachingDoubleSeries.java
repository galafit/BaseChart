package data.series;

/**
 * Created by galafit on 25/11/17.
 */
public class CachingDoubleSeries implements DoubleSeries {
    private DoubleSeries inputData;
    private DoubleArrayList cachedData;
    private boolean isCashingEnabled = true;


    public CachingDoubleSeries(DoubleSeries inputData) {
        this.inputData = inputData;
        cachedData = new DoubleArrayList(inputData.size());
        cacheData();
    }

    private void cacheData() {
        if (cachedData.size()  < inputData.size()) {
            for (int i = cachedData.size(); i < inputData.size(); i++) {
                cachedData.add(inputData.get(i));
            }
        }
    }

    public void disableCashing() {
        isCashingEnabled = false;
        cachedData = null;
    }

    @Override
    public double get(int index) {
        if(isCashingEnabled) {
            return cachedData.get(index);
        }
        return inputData.get(index);
    }


    @Override
    public int size() {
        if(isCashingEnabled) {
            cacheData();
            return cachedData.size();
        }
        return inputData.size();
    }
}
