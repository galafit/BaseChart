package com.biorecorder.basechart.chart.config;

import com.biorecorder.basechart.chart.BColor;
import com.biorecorder.basechart.chart.BStroke;
import com.biorecorder.basechart.chart.Margin;
import com.biorecorder.basechart.chart.Range;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Created by galafit on 2/12/17.
 */
public class ScrollableChartConfig {
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;

    private SimpleChartConfig chartConfig;
    private SimpleChartConfig previewConfig;
    private int gapBetweenChartPreview; //px
    private Margin margin;
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Map<Integer, Double> scrollExtents = new Hashtable<Integer, Double>(2);
    private Range previewMinMax;

    public ScrollableChartConfig() {
        this(1, true);
    }

    public ScrollableChartConfig(boolean isDateTime) {
        this(1, isDateTime);
    }

    public ScrollableChartConfig(int theme, boolean isDateTime) {
        AxisConfig leftAxisConfig = new AxisConfig(AxisOrientation.LEFT);
        AxisConfig rightAxisConfig = new AxisConfig(AxisOrientation.RIGHT);
        leftAxisConfig.setMinMaxRoundingEnable(true);
        leftAxisConfig.setLabelInside(true);
        leftAxisConfig.setTickMarkInsideSize(3);
        leftAxisConfig.setTickMarkOutsideSize(0);

        rightAxisConfig.setLabelInside(true);
        rightAxisConfig.setTickMarkInsideSize(3);
        rightAxisConfig.setTickMarkOutsideSize(0);
        rightAxisConfig.setMinMaxRoundingEnable(true);
        rightAxisConfig.setGridLineStroke(new BStroke(1));
        chartConfig = new SimpleChartConfig(false, false, leftAxisConfig, rightAxisConfig);
        previewConfig = new SimpleChartConfig(true, false, leftAxisConfig, rightAxisConfig);

        if(isDateTime) {
            chartConfig.getXConfig(0).setAxisType(AxisType.TIME);
            chartConfig.getXConfig(1).setAxisType(AxisType.TIME);
            previewConfig.getXConfig(0).setAxisType(AxisType.TIME);
            previewConfig.getXConfig(1).setAxisType(AxisType.TIME);
        }

        if(theme == DARK_THEME) {
            BColor bgColor = BColor.BLACK;
            BColor marginColor = bgColor;
            BColor textColor = BColor.BROWN;
            chartConfig.setBackground(bgColor);
            chartConfig.setMarginColor(marginColor);
            chartConfig.getLegendConfig().setBackgroundColor(bgColor);
            chartConfig.getLegendConfig().setTextColor(textColor);
            chartConfig.setTitleColor(textColor);

            BColor previewBgColor = new BColor(10, 10, 25);
            BColor previewMarginColor = previewBgColor;
            previewConfig.setBackground(previewBgColor);
            previewConfig.setMarginColor(previewBgColor);
            previewConfig.getLegendConfig().setBackgroundColor(previewBgColor);
            previewConfig.getLegendConfig().setTextColor(textColor);
            previewConfig.setTitleColor(textColor);
        }
    }

    public SimpleChartConfig getChartConfig() {
        return chartConfig;
    }

    public SimpleChartConfig getPreviewConfig() {
        return previewConfig;
    }

    public int getGapBetweenChartPreview() {
        return gapBetweenChartPreview;
    }

    public void setGapBetweenChartPreview(int gapBetweenChartPreview) {
        this.gapBetweenChartPreview = gapBetweenChartPreview;
    }

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    public void setPreviewMinMax(Range minMax) {
      previewMinMax = minMax;
        for (int i = 0; i < previewConfig.getNumberOfXAxis(); i++) {
            previewConfig.setXMinMax(i, minMax);
        }
    }

    public Range getPreviewMinMax() {
        return previewMinMax;
    }

    public ScrollConfig getScrollConfig() {
        return scrollConfig;
    }

    public Double getScrollExtent(int xAxisIndex) {
        return scrollExtents.get(xAxisIndex);
    }

    public double[] getScrollsExtents() {
        double[] extents = new double[scrollExtents.keySet().size()];
        int i = 0;
        for (Integer xAxisIndex : scrollExtents.keySet()) {
            extents[i] = scrollExtents.get(xAxisIndex);
            i++;
        }
        return extents;
    }

    public void addScroll(int xAxisIndex, double extent) {
        scrollExtents.put(xAxisIndex, extent);
    }

    public Set<Integer> getXAxisWithScroll() {
        return scrollExtents.keySet();
    }

    public void addPreviewStack(int weight) {
        previewConfig.addStack(weight);
    }

    public void addPreviewStack() {
        addPreviewStack(SimpleChartConfig.DEFAULT_WEIGHT);
    }

    public void addChartStack(int weight) {
        chartConfig.addStack(weight);

    }

    public void addChartStack() {
        addChartStack(SimpleChartConfig.DEFAULT_WEIGHT);
    }
}
