import base.Range;
import base.config.ScrollableChartConfig;
import base.config.SimpleChartConfig;
import base.config.ScrollConfig;
import base.config.traces.TraceConfig;
import data.BaseDataSet;
import data.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by galafit on 6/10/17.
 */
public class ChartConfig {
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;
    private ArrayList<BaseDataSet> chartData = new ArrayList<BaseDataSet>();
    private ArrayList<BaseDataSet> previewData = new ArrayList<BaseDataSet>();
    private ScrollableChartConfig scrollableChartConfig;

    private boolean isDataCropEnable = true;
    private boolean isGroupingEnable = true;
    private int compression = 0;

    public ChartConfig() {
        this(1);
    }

    public ChartConfig(int theme) {
        scrollableChartConfig = new ScrollableChartConfig(theme);
    }

    public ScrollableChartConfig getScrollableChartConfig() {
        return scrollableChartConfig;
    }

    public SimpleChartConfig getChartConfig() {
        return scrollableChartConfig.getChartConfig();
    }

    public SimpleChartConfig getPreviewConfig() {
        return scrollableChartConfig.getPreviewConfig();
    }

    public List<BaseDataSet> getOriginalChartData() {
        return chartData;
    }

    public List<BaseDataSet> getOriginalPreviewData() {
        return previewData;
    }

    public int getCompression() {
        return compression;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }

    public ScrollConfig getScrollConfig() {
        return scrollableChartConfig.getScrollConfig();
    }

    public Double getScrollExtent(int xAxisIndex) {
        return scrollableChartConfig.getScrollExtent(xAxisIndex);
    }

    public void addScroll(int xAxisIndex, double extent) {
        scrollableChartConfig.addScroll(xAxisIndex, extent);
    }

    public Set<Integer> getXAxisWithScroll() {
        return scrollableChartConfig.getXAxisWithScroll();
    }

    public double[] getScrollsExtents() {
        return  scrollableChartConfig.getScrollsExtents();
    }

    public void setPreviewMinMax(Range minMax) {
        scrollableChartConfig.setPreviewMinMax(minMax);
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

    public void addStack(int weight) {
        scrollableChartConfig.getChartConfig().addStack(weight);

    }

    public void addStack() {
        addStack(SimpleChartConfig.DEFAULT_WEIGHT);
    }

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data data, String name, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        chartData.add(data.getDataSet());
        scrollableChartConfig.getChartConfig().addTrace(traceConfig, chartData.size() - 1, name,  isXAxisOpposite, isYAxisOpposite);
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

    public void addPreviewStack(int weight) {
        scrollableChartConfig.getPreviewConfig().addStack(weight);

    }

    public void addPreviewStack() {
        addPreviewStack(SimpleChartConfig.DEFAULT_WEIGHT);
    }

    /**
     * add trace to the last preview stack
     */
    public void addPreviewTrace(TraceConfig traceConfig, Data data, String name,  boolean isXAxisOpposite, boolean isYAxisOpposite) {
        previewData.add(data.getDataSet());
        scrollableChartConfig.getPreviewConfig().addTrace(traceConfig, previewData.size() - 1, name,  isXAxisOpposite, isYAxisOpposite);
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
