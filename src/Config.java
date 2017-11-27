import base.config.ChartConfig;
import base.config.ScrollConfig;
import base.config.traces.TraceConfig;
import data.BaseDataSet;
import data.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by galafit on 6/10/17.
 */
public class Config {
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;
    private ArrayList<BaseDataSet> chartData = new ArrayList<BaseDataSet>();
    private ArrayList<BaseDataSet> previewData = new ArrayList<BaseDataSet>();

    private boolean isPreviewEnable = false;
    private ChartConfig chartConfig = new ChartConfig();
    private ChartConfig previewConfig = new ChartConfig();
    private ScrollConfig scrollConfig = new ScrollConfig();
    private double[] scrollExtents = new double[2];

    private boolean isCropEnable = true;
    private boolean isGroupingEnable = true;
    private int compression = 0;



    public Config() {
        this(DARK_THEME);
    }

    public Config(int theme) {
        if(theme == DARK_THEME) {
            Color bgColor = new Color(20, 20, 30);
            Color marginColor = new Color(20, 20, 20);
            Color textColor = new Color(200, 200, 200);
            Color legendBorderColor = new Color(50, 50, 50);
            Color legendBgColor = new Color(30, 30, 30);
            legendBgColor = bgColor;
            chartConfig.setBackground(bgColor);
            chartConfig.setMarginColor(marginColor);
            chartConfig.getLegendConfig().setBackground(legendBgColor);
            chartConfig.getLegendConfig().setBorderWidth(1);
            chartConfig.getLegendConfig().setBorderColor(legendBorderColor);
            chartConfig.getLegendConfig().getTextStyle().setFontColor(textColor);
            chartConfig.getTitleTextStyle().setFontColor(textColor);

            previewConfig.setBackground(bgColor);
            previewConfig.setMarginColor(marginColor);
            previewConfig.getLegendConfig().setBackground(legendBgColor);
            previewConfig.getLegendConfig().setBorderWidth(1);
            previewConfig.getLegendConfig().setBorderColor(legendBorderColor);
            previewConfig.getLegendConfig().getTextStyle().setFontColor(textColor);
            previewConfig.getTitleTextStyle().setFontColor(textColor);
        }
    }

    public ChartConfig getChartConfig() {
        return chartConfig;
    }

    public ChartConfig getPreviewConfig() {
        return previewConfig;
    }

    public List<BaseDataSet> getChartData() {
        return chartData;
    }

    public List<BaseDataSet> getPreviewData() {
        return previewData;
    }

    public int getCompression() {
        return compression;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }

    public ScrollConfig getScrollConfig() {
        return scrollConfig;
    }

    public double getScrollExtent(int xAxisIndex) {
        return scrollExtents[xAxisIndex];
    }

    public void setScrollExtent(int xAxisIndex, double extent) {
        scrollExtents[xAxisIndex] =  extent;
    }

    public boolean isCropEnable() {
        return isCropEnable;
    }

    public void setCropEnable(boolean isCropEnable) {
        this.isCropEnable = isCropEnable;
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
        chartConfig.addStack(weight);

    }

    public void addStack() {
        addStack(ChartConfig.DEFAULT_WEIGHT);
    }

    /**
     * add trace to the last chart stack
     */
    public void addTrace(TraceConfig traceConfig, Data data, String name, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        chartData.add(data.getDataSet());
        chartConfig.addTrace(traceConfig, chartData.size() - 1, name,  isXAxisOpposite, isYAxisOpposite);
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

    public boolean isPreviewEnable() {
        return isPreviewEnable;
    }

    public void enablePreview(boolean isPreviewEnable) {
        this.isPreviewEnable = isPreviewEnable;
    }

    public void addPreviewStack(int weight) {
        previewConfig.addStack(weight);

    }

    public void addPreviewStack() {
        addPreviewStack(ChartConfig.DEFAULT_WEIGHT);
    }

    /**
     * add trace to the last preview stack
     */
    public void addPreviewTrace(TraceConfig traceConfig, Data data, String name,  boolean isXAxisOpposite, boolean isYAxisOpposite) {
        previewData.add(data.getDataSet());
        previewConfig.addTrace(traceConfig, previewData.size() - 1, name,  isXAxisOpposite, isYAxisOpposite);
        isPreviewEnable = true;
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