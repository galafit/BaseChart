import base.config.ChartConfig;
import base.config.traces.TraceConfig;

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
            chartConfig.setBackground(bgColor);
            chartConfig.setMarginColor(marginColor);
            chartConfig.getLegendConfig().setBackground(marginColor);
            chartConfig.getLegendConfig().setBorderWidth(1);
            chartConfig.getLegendConfig().getTextStyle().setFontColor(textColor);
            chartConfig.getTitleTextStyle().setFontColor(textColor);

            previewConfig.setBackground(bgColor);
            previewConfig.setMarginColor(marginColor);
            previewConfig.getLegendConfig().setBackground(marginColor);
            previewConfig.getLegendConfig().setBorderWidth(1);
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
     * @param traceConfig
     * @param isBottomXAxis
     * @param isLeftYAxis
     */
    public void addTrace(TraceConfig traceConfig, boolean isBottomXAxis, boolean isLeftYAxis) {
        chartConfig.addTrace(traceConfig, isBottomXAxis, isLeftYAxis);
    }

    /**
     * add trace to the last chart stack
     * @param traceConfig
     */
    public void addTrace(TraceConfig traceConfig) {
        addTrace(traceConfig, true, true);
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
     * @param traceConfig
     * @param isBottomXAxis
     * @param isLeftYAxis
     */
    public void addPreviewTrace(TraceConfig traceConfig, boolean isBottomXAxis, boolean isLeftYAxis) {
        previewConfig.addTrace(traceConfig, isBottomXAxis, isLeftYAxis);
    }

    /**
     * add trace to the last preview stack
     * @param traceConfig
     */
    public void addPreviewTrace(TraceConfig traceConfig) {
        addPreviewTrace(traceConfig, true, true);
    }




}
