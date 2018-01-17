package com.biorecorder.basechart;

import com.biorecorder.basechart.chart.config.ScrollableChartConfig;
import com.biorecorder.basechart.chart.config.traces.TraceConfig;
import com.biorecorder.basechart.data.BaseData;
import com.biorecorder.basechart.data.Data;

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
    private ArrayList<Float> previewGroupingIntervals = new ArrayList<Float>();
    private List<BaseData> chartData = new ArrayList<BaseData>();
    private List<BaseData> previewData = new ArrayList<BaseData>();
    // true for pc and false for phone
    private boolean isChartGroupedDatCachingEnable = true;

    public ChartConfig(boolean isDateTime) {
        super(isDateTime);
    }

    public ChartConfig(int theme, boolean isDateTime) {
        super(theme, isDateTime);
    }

    public boolean isPreviewEnable() {
        if(getScrollsExtents().length > 0 || getPreviewConfig().getTraceCounter() > 0) {
            return true;
        }
        return false;
    }

    public boolean isChartGroupedDatCachingEnable() {
        return isChartGroupedDatCachingEnable;
    }

    public void setChartGroupedDatCachingEnable(boolean isChartGroupedDatCachingEnable) {
        this.isChartGroupedDatCachingEnable = isChartGroupedDatCachingEnable;
    }

    public List<Float> getPreviewGroupingIntervals() {
        return previewGroupingIntervals;
    }

    public void addPreviewGroupingInterval(float groupingInterval) {
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

    public List<BaseData> getChartData() {
        return chartData;
    }

    public List<BaseData> getPreviewData() {
        return previewData;
    }

    /*********************************************
     *              CHART CONFIG
     *********************************************/

    /**
     * add trace to the last com.biorecorder.basechart.chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data traceData, String name, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        chartData.add(traceData.getDataSet());
        getChartConfig().addTrace(traceConfig, chartData.size() - 1, name,  isXAxisOpposite, isYAxisOpposite);
    }

    /**
     * add trace to the last com.biorecorder.basechart.chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data traceData, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        addTrace(traceConfig, traceData, null, isXAxisOpposite, isYAxisOpposite);
    }

    /**
     * add trace to the last com.biorecorder.basechart.chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data traceData, String name) {
        addTrace(traceConfig, traceData, name, false, false);
    }

    /**
     * add trace to the last com.biorecorder.basechart.chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data traceData) {
        addTrace(traceConfig, traceData, null, false, false);
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
