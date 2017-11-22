package data.series;

/**
 * Class is designed to store/cache a computed input data  and to give quick access to them.
 * Input data could be a filter, function and so on
 */

public class CachingIntSeries implements IntSeries {
    protected IntSeries inputData;
    protected IntArrayList cachedData;

    public CachingIntSeries(IntSeries inputData) {
        this.inputData = inputData;
        cachedData = new IntArrayList(inputData.size());
        cacheData();
    }


    private void cacheData() {
        if (cachedData.size()  < inputData.size()) {
            for (int i = cachedData.size(); i < inputData.size(); i++) {
                cachedData.add(inputData.get(i));
            }
        }
    }

    @Override
    public int get(int index) {
        return cachedData.get(index);
    }


    @Override
    public int size() {
        cacheData();
        return cachedData.size();
    }
}

