import base.config.ChartConfig;
import base.config.traces.TraceConfig;
import data.Data;

import java.awt.*;

/**
 * Created by galafit on 6/10/17.
 */
public class Config {
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;

    private boolean isPreviewEnable = false;
    private ChartConfig chartConfig = new ChartConfig();
    private ChartConfig previewConfig = new ChartConfig();
    private long chartWidth;

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

    public long getChartWidth() {
        return chartWidth;
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
        chartConfig.addTrace(traceConfig, data.getDataSet(), name,  isXAxisOpposite, isYAxisOpposite);
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

    public void enablePreview(long chartWidth) {
        this.chartWidth = chartWidth;
        isPreviewEnable = true;
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
        previewConfig.addTrace(traceConfig, data.getDataSet(), name,  isXAxisOpposite, isYAxisOpposite);
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
