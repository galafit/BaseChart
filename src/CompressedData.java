import main.chart.compressors.CompressFunction;
import main.chart.compressors.SumCompression;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 05.07.17.
 */
public class CompressedData extends PeriodicData  {
    private PeriodicData periodicData;
    private int compressRatio = 1;
    private CompressFunction compressFunction;
    private double[] buffer;

    public CompressedData(PeriodicData periodicData, int compressRatio, CompressFunction compressFunction) {
        super(periodicData.getStartValue(), periodicData.getSamplesPerUnit() / compressRatio);
        this.periodicData = periodicData;
        if (compressRatio < 1){
            compressRatio = 1;
        }
        this.compressRatio = compressRatio;
        this.compressFunction = compressFunction;
        buffer = new double[compressRatio];
        compress();
    }

    public CompressedData(PeriodicData periodicData, int compressRatio){this(periodicData, compressRatio, new SumCompression());
    }


    public void update(){
        compress();
    }

    private void compress(){

        List<Double> dataList = periodicData.getDataList();
        System.out.println("DataListSize = " + dataList.size() + " ratio = " + compressRatio);
        data = new ArrayList<Double>();
        int count = 0;
        for (int i = 0; i < dataList.size(); i++) {
            buffer[count] = dataList.get(i);
            count++;
            if (count == compressRatio) {
                data.add(compressFunction.compress(buffer));
                count = 0;
            }
        }
        //setRange(0,data.size() - 1);
        startIndex = 0;
        window = data.size();
        System.out.println("Data size = " + data.size());
    }

    @Override
    public void setRange(int startIndex, int window) {
        //super.setRange(startIndex, window);
    }
}
