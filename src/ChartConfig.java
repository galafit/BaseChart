
import base.config.ScrollableChartConfig;
import base.config.traces.TraceConfig;
import data.BaseDataSet;
import data.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 6/10/17.
 */
public class ChartConfig extends ScrollableChartConfig {
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;

    private boolean isDataCropEnable = true;
    private boolean isGroupingEnable = true;
    private ArrayList<Double> previewGroupingIntervals = new ArrayList<Double>();
    private List<BaseDataSet> chartData = new ArrayList<BaseDataSet>();
    private List<BaseDataSet> previewData = new ArrayList<BaseDataSet>();
    // true for pc and false for phone
    private boolean isChartGroupedDatCachingEnable = false;

    public boolean isChartGroupedDatCachingEnable() {
        return isChartGroupedDatCachingEnable;
    }

    public void setChartGroupedDatCachingEnable(boolean isChartGroupedDatCachingEnable) {
        this.isChartGroupedDatCachingEnable = isChartGroupedDatCachingEnable;
    }

    public List<Double> getPreviewGroupingIntervals() {
        return previewGroupingIntervals;
    }

    public void addPreviewGroupingInterval(double groupingInterval) {
        previewGroupingIntervals.add(groupingInterval);
    }

    public boolean isCropEnable() {
        return isDataCropEnable;
    }

    public void setCropEnable(boolean isCropEnable) {
        this.isDataCropEnable = isCropEnable;
    }

    public boolean isGroupingEnable() {
        return isGroupingEnable;
    }

    public void setGroupingEnable(boolean isGroupingEnable) {
        this.isGroupingEnable = isGroupingEnable;
    }

    public List<BaseDataSet> getChartData() {
        return chartData;
    }

    public List<BaseDataSet> getPreviewData() {
        return previewData;
    }

    /*********************************************
     *              CHART CONFIG
     *********************************************/

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data traceData, String name, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        chartData.add(traceData.getDataSet());
        getChartConfig().addTrace(traceConfig, chartData.size() - 1, name,  isXAxisOpposite, isYAxisOpposite);
    }

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data data, String name) {
        addTrace(traceConfig, data, name, false, false);
    }

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data data) {
        addTrace(traceConfig, data, null, false, false);
    }


    /*********************************************
     *              PREVIEW CONFIG
     *********************************************/


    /**
     * add trace to the last preview stack
     */
    public void addPreviewTrace(TraceConfig traceConfig, Data traceData, String name,  boolean isXAxisOpposite, boolean isYAxisOpposite) {
        previewData.add(traceData.getDataSet());
        getPreviewConfig().addTrace(traceConfig, previewData.size() - 1, name,  isXAxisOpposite, isYAxisOpposite);
    }

    /**
     * add trace to the last preview stack
     */
    public void addPreviewTrace(TraceConfig traceConfig, Data data, String name) {
        addPreviewTrace(traceConfig, data, name, false, false);
    }

    /**
     * add trace to the last preview stack
     */
    public void addPreviewTrace(TraceConfig traceConfig, Data data) {
        addPreviewTrace(traceConfig, data, null, false, false);
    }
}
