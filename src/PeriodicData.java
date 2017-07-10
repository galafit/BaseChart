import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 26.06.17.
 */
public class PeriodicData extends AbstractList<DataItem> implements SliceDataList {
    private double startValue;
    protected List<Double> data;
    private double samplesPerUnit;

    protected int window = -1;
    protected int startIndex;
    private Double yMin = null;
    private Double yMax = null;

    public PeriodicData(double startValue, double samplesPerUnit) {
           this(new ArrayList<Double>(), startValue, samplesPerUnit);
    }

    public PeriodicData(List<Double> data, double startValue, double samplesPerUnit) {
        this.startValue = startValue;
        this.samplesPerUnit = samplesPerUnit;
        this.data = data;

    }

    public void setRange(double startValue, double endValue){
        setRange(getMaxIndex(startValue),getMaxIndex(endValue) - getMaxIndex(startValue));
        setMinMax();
    }

    public void setRange(int startIndex, int window){

        if (startIndex <0){
            startIndex = 0;
            window = window + startIndex;
        }
        if (startIndex >= data.size()){
            window = -1;
        }
        if (window + startIndex >= data.size()){
            window = data.size() - startIndex;
        }
        this.startIndex = startIndex;
        this.window = window;
        setMinMax();
    }

    private void setMinMax(){
        if(window >0) {
            double yMin = Double.MAX_VALUE;
            double yMax = -Double.MAX_VALUE;
            System.out.println("StartIndex = " + startIndex + " window = " + window);
            for (int i = startIndex; i < startIndex + window; i++) {
                yMin = Math.min(yMin, data.get(i));
                yMax = Math.max (yMax, data.get(i));
            }
            this.yMin = yMin;
            this.yMax = yMax;
        } else {
            yMin = null;
            yMax = null;
        }

    }

    private int getMinIndex(double startValue){
        int index = getIndex(startValue) - 1;
        return Math.max(0,index);
    }

    private int getMaxIndex(double endValue){
        int index = getIndex(endValue) + 1;
        return Math.min(index, data.size());
    }

    public int getFullSize() {
        return data.size();
    }


    @Override
    public int size() {
        if (window <= 0){
            return 0;
        }
        return window - 1;
    }

    @Override
    public Double getXmin() {
        return get(0).getX();
    }

    @Override
    public Double getXmax() {
        return get(data.size() - 1).getX();
    }

    @Override
    public Double getYmin() {
        if(yMin == null) {
            setMinMax();
        }
        return yMin;
    }

    @Override
    public Double getYmax() {
        if(yMax == null) {
            setMinMax();
        }
        return yMax;
    }

    public void addData(double dataValue){
        data.add(dataValue);
    }

    public int getIndex (double value){
        return (int)((value - startValue) * samplesPerUnit);
    }

    public List<Double> getDataList(){
        return data;
    }

    public double getStartValue() {
        return startValue;
    }

    public double getSamplesPerUnit() {
        return samplesPerUnit;
    }

    @Override
    public DataItem get(int index) {
        return new DataItem() {
            @Override
            public double getX() {
                return startValue + (startIndex + index) / samplesPerUnit;
            }

            @Override
            public double getY() {
                int rangeIndex = index + startIndex;
                if (rangeIndex >= data.size()){
                    rangeIndex = data.size() - 1;
                }
                return data.get(rangeIndex);
            }
        };
    }

}
