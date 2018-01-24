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
    private final BColor CYAN = new BColor(0, 200, 230);
    private final BColor BLUE = new BColor(100, 120, 250);
    private final BColor MAGENTA = new BColor(165, 80, 190); //new BColor(200, 40, 250);
    private final BColor GREEN = new BColor(120, 250, 123);//new BColor(77, 184, 118);//new BColor(0, 204, 31);
    private final BColor RED = new BColor(250, 64, 82);//new BColor(191, 60, 54);
    private final BColor ORANGE = new BColor(200, 80, 0);//new BColor(173, 105, 49);
    private final BColor YELLOW = new BColor(252, 177, 48);
    private final BColor PINK = BColor.PINK; //new BColor(200, 150, 150);


    private final BColor DARK_CYAN = new BColor(60, 170, 180);
    private final BColor DARK_BLUE = new BColor(40, 120, 230); //new BColor(37, 100, 250);
    private final BColor DARK_MAGENTA = new BColor(150, 80, 150);
    private final BColor DARK_GREEN = new BColor(40, 180, 15);
    private final BColor DARK_RED = new BColor(200, 10, 70);
    private final BColor DARK_ORANGE = new BColor(138, 85, 74);//new BColor(173, 105, 49);
    private final BColor DARK_YELLOW = new BColor(255, 127, 15);
    private final BColor DARK_PINK = new BColor(100, 110, 140);


    public static final BColor BROWN = new BColor(130, 110, 80);//new BColor(120, 94, 50); //new BColor(145, 94, 32);//new BColor(163, 106, 36); //new BColor(125, 81, 26);
    public static final BColor DARK_BROWN = new BColor(60, 55, 35); // new BColor(64, 56, 40);

//  public static final BColor ORANGE = new BColor(200, 102, 0);//new BColor(255, 153, 0);

    public static final int DARK_THEME = 1;
    public static final int WHITE_THEME = 2;
    public static final int GRAY_THEME = 3;


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
        BColor[] lightTraceColors = {BLUE, MAGENTA, PINK, RED, ORANGE, YELLOW, GREEN, CYAN};
       // BColor[] darkTraceColors = {BColor.BLUE, BColor.MAGENTA, BColor.GRAY, BColor.RED, BColor.CYAN, BColor.GREEN, BColor.YELLOW, BColor.PINK};
        BColor[] darkTraceColors = {DARK_BLUE, DARK_MAGENTA, DARK_PINK, DARK_RED, DARK_ORANGE, DARK_YELLOW, DARK_GREEN, DARK_CYAN};

        // WHITE_THEME
        BColor chartBgColor = new BColor(255, 255, 255); // new BColor(160, 160, 160);
        BColor chartMarginColor = chartBgColor;

        BColor previewBgColor = new BColor(245, 245, 250); // //new BColor(150, 150, 150);
        BColor previewMarginColor = previewBgColor;

        BColor textColor =new BColor(110, 100, 80);// new BColor(90, 100, 120);
        BColor axisColor = textColor;
        BColor gridColor = new BColor(215, 208, 200);
        BColor[] traceColors = darkTraceColors;

        if(theme == DARK_THEME) {
            chartBgColor = BColor.BLACK;
            chartMarginColor = chartBgColor;
            textColor = BROWN;

            previewBgColor = new BColor(12, 18, 28);
            previewMarginColor = previewBgColor;

            axisColor = BROWN;
            gridColor = DARK_BROWN;
            traceColors = lightTraceColors;
        }

        AxisConfig leftAxisConfig = new AxisConfig(AxisOrientation.LEFT);
        AxisConfig rightAxisConfig = new AxisConfig(AxisOrientation.RIGHT);
        leftAxisConfig.setMinMaxRoundingEnable(true);
        leftAxisConfig.setLabelInside(true);
        leftAxisConfig.setTickMarkInsideSize(3);
        leftAxisConfig.setTickMarkOutsideSize(0);
        leftAxisConfig.setColor(axisColor);
        leftAxisConfig.setGridColor(gridColor);
        leftAxisConfig.setMinorGridColor(gridColor);

        rightAxisConfig.setLabelInside(true);
        rightAxisConfig.setTickMarkInsideSize(3);
        rightAxisConfig.setTickMarkOutsideSize(0);
        rightAxisConfig.setMinMaxRoundingEnable(true);
        rightAxisConfig.setGridLineStroke(new BStroke(1));
        rightAxisConfig.setColor(axisColor);
        rightAxisConfig.setGridColor(gridColor);
        rightAxisConfig.setMinorGridColor(gridColor);

        chartConfig = new SimpleChartConfig(false, false, leftAxisConfig, rightAxisConfig);
        previewConfig = new SimpleChartConfig(true, false, leftAxisConfig, rightAxisConfig);

        AxisConfig chartBottomAxisConfig = chartConfig.getXConfig(0);
        chartBottomAxisConfig.setColor(axisColor);
        chartBottomAxisConfig.setGridColor(gridColor);
        chartBottomAxisConfig.setMinorGridColor(gridColor);

        AxisConfig chartTopAxisConfig = chartConfig.getXConfig(1);
        chartTopAxisConfig.setColor(axisColor);
        chartTopAxisConfig.setGridColor(gridColor);
        chartTopAxisConfig.setMinorGridColor(gridColor);

        AxisConfig previewBottomAxisConfig = previewConfig.getXConfig(0);
        previewBottomAxisConfig.setColor(axisColor);
        previewBottomAxisConfig.setGridColor(gridColor);
        previewBottomAxisConfig.setMinorGridColor(gridColor);

        AxisConfig previewTopAxisConfig = previewConfig.getXConfig(1);
        previewTopAxisConfig.setColor(axisColor);
        previewTopAxisConfig.setGridColor(gridColor);
        previewTopAxisConfig.setMinorGridColor(gridColor);

        if(isDateTime) {
            chartBottomAxisConfig.setAxisType(AxisType.TIME);
            chartTopAxisConfig.setAxisType(AxisType.TIME);

            previewBottomAxisConfig.setAxisType(AxisType.TIME);
            previewTopAxisConfig.setAxisType(AxisType.TIME);
         }

        chartConfig.setBackground(chartBgColor);
        chartConfig.setMarginColor(chartMarginColor);
        chartConfig.getLegendConfig().setBackgroundColor(chartBgColor);
        chartConfig.getLegendConfig().setTextColor(textColor);
        chartConfig.setTitleColor(textColor);
        chartConfig.setDefaultTraceColors(traceColors);

        previewConfig.setBackground(previewBgColor);
        previewConfig.setMarginColor(previewMarginColor);
        previewConfig.getLegendConfig().setBackgroundColor(previewBgColor);
        previewConfig.getLegendConfig().setTextColor(textColor);
        previewConfig.setTitleColor(textColor);
        previewConfig.setDefaultTraceColors(traceColors);
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
