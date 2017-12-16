
import base.config.ScrollableChartConfig;
import base.config.traces.TraceConfig;
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
    // when number of chart visible points will be bigger chart grouping will be activated
    private int chartMaxVisiblePoints = 1000;

    public int getChartMaxVisiblePoints() {
        return chartMaxVisiblePoints;
    }

    public void setChartMaxVisiblePoints(int chartMaxVisiblePoints) {
        this.chartMaxVisiblePoints = chartMaxVisiblePoints;
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

    /*********************************************
     *              CHART CONFIG
     *********************************************/

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data traceData, String name, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        getChartConfig().addTrace(traceConfig, traceData.getDataSet(), name,  isXAxisOpposite, isYAxisOpposite);
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
        getPreviewConfig().addTrace(traceConfig, traceData.getDataSet(), name,  isXAxisOpposite, isYAxisOpposite);
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
