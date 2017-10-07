package base.config;

import base.config.axis.AxisConfig;
import base.config.axis.Orientation;
import base.config.general.Margin;
import base.config.general.TextStyle;
import base.config.traces.TraceConfig;
import base.Range;

import java.awt.*;
import java.util.*;

/**
 * Created by galafit on 18/8/17.
 */
public class ChartConfig {
    public static final int DEFAULT_WEIGHT = 10;
    private final Color GREY = new Color(150, 150, 150);
    private final Color BROWN = new Color(200, 102, 0);
    private final Color ORANGE = new Color(255, 153, 0);
    private Color[] traceColors = {Color.MAGENTA, Color.RED, ORANGE, Color.CYAN, Color.PINK};
    private boolean isBottomXAxisHasTraces = false;
    private boolean isTopXAxisHasTraces = false;


    private String title = "base.chart.BaseChart title";
    private Color background;
    private Color marginColor;
    private Margin margin = new Margin(20, 10, 50, 50);
    private TextStyle titleTextStyle = new TextStyle();
    private LegendConfig legendConfig = new LegendConfig();
    private TooltipConfig tooltipConfig = new TooltipConfig();
    private CrosshairConfig crosshairConfig = new CrosshairConfig();
    private ArrayList<TraceConfig> traces = new ArrayList<TraceConfig>();
    private ArrayList<Integer> weights = new ArrayList<Integer>();
    /*
     * 2 X-base.axis: 0(even) - BOTTOM and 1(odd) - TOP
     * 2 Y-base.axis for every section(stack): even - LEFT and odd - RIGHT;
     * All LEFT and RIGHT Y-base.axis are stacked.
     * If there is no trace associated with the base.axis... base.axis is invisible.
     **/
    private ArrayList<AxisConfig> xAxisConfigs = new ArrayList<AxisConfig>();
    private ArrayList<AxisConfig> yAxisConfigs = new ArrayList<AxisConfig>();


    public ChartConfig() {
        getTitleTextStyle().setBold(true);
        getTitleTextStyle().setFontSize(14);
        weights.add(DEFAULT_WEIGHT);

        AxisConfig axisConfig = new AxisConfig(Orientation.BOTTOM);
        axisConfig.getMinorGridLineConfig().setWidth(0);
        axisConfig.getGridLineConfig().setWidth(1);
        xAxisConfigs.add(axisConfig);

        axisConfig = new AxisConfig(Orientation.TOP);
        axisConfig.getMinorGridLineConfig().setWidth(0);
        axisConfig.getGridLineConfig().setWidth(0);
        xAxisConfigs.add(axisConfig);

        axisConfig = new AxisConfig(Orientation.LEFT);
        axisConfig.getMinorGridLineConfig().setWidth(0);
        axisConfig.getGridLineConfig().setWidth(1);
        yAxisConfigs.add(axisConfig);

        axisConfig = new AxisConfig(Orientation.RIGHT);
        axisConfig.getMinorGridLineConfig().setWidth(0);
        axisConfig.getGridLineConfig().setWidth(0);
        yAxisConfigs.add(axisConfig);
    }

    public AxisConfig getTopAxisConfig() {
        return xAxisConfigs.get(1);
    }

    public AxisConfig getBottomAxisConfig() {
        return xAxisConfigs.get(0);
    }

    public AxisConfig getLeftAxisConfig(int stackIndex) {
        return yAxisConfigs.get(stackIndex * 2);
    }

    public AxisConfig getRightAxisConfig(int stackIndex) {
        return yAxisConfigs.get(stackIndex * 2 + 1);
    }

    public int getStacksAmount() {
        return yAxisConfigs.size() / 2;
    }

    public AxisConfig getXAxisConfig(int axisIndex) {
        return xAxisConfigs.get(axisIndex);
    }

    public AxisConfig getYAxisConfig(int axisIndex) {
        return yAxisConfigs.get(axisIndex);
    }

    public int getSumWeight() {
        int weightSum = 0;
        for (Integer weight : weights) {
            weightSum += weight;
        }
        return weightSum;
    }


    public Range getYAxisStartEnd(int yAxisIndex, Rectangle area) {
        int weightSum = getSumWeight();

        int weightSumTillYAxis = 0;
        for (int i = 0; i < yAxisIndex / 2 ; i++) {
            weightSumTillYAxis += weights.get(i);
        }

        int yAxisWeight = weights.get(yAxisIndex / 2);
        int axisHeight = area.height *  yAxisWeight / weightSum;

        int end = area.y + area.height * weightSumTillYAxis / weightSum;
        int start = end + axisHeight;
        return new Range(start, end, true);
    }

    public boolean isBothXAxisHaveTraces() {
        return isBottomXAxisHasTraces && isTopXAxisHasTraces;
    }

    public void addStack(int weight) {
        AxisConfig axisConfig = new AxisConfig(Orientation.LEFT);
        axisConfig.getMinorGridLineConfig().setWidth(0);
        axisConfig.getGridLineConfig().setWidth(1);
        yAxisConfigs.add(axisConfig);

        axisConfig = new AxisConfig(Orientation.RIGHT);
        axisConfig.getMinorGridLineConfig().setWidth(0);
        axisConfig.getGridLineConfig().setWidth(0);
        yAxisConfigs.add(axisConfig);
        weights.add(weight);
    }


    // add trace to the last stack
    public void addTrace(TraceConfig traceConfig, boolean isBottomXAxis, boolean isLeftYAxis) {
        int xAxisIndex = isBottomXAxis ? 0 : 1;
        int yAxisIndex = isLeftYAxis ? yAxisConfigs.size() - 2 : yAxisConfigs.size() - 1;
        if(isBottomXAxis) {
            isBottomXAxisHasTraces = true;
        } else {
            isTopXAxisHasTraces = true;
        }

        xAxisConfigs.get(xAxisIndex).setVisible(true);
        yAxisConfigs.get(yAxisIndex).setVisible(true);
        if(traceConfig.getName() == null) {
            traceConfig.setName("Trace "+traces.size());
        }
        if(traceConfig.getColor() == null) {
            int colorIndex = traces.size() % traceColors.length;
            traceConfig.setColor(traceColors[colorIndex]);
        }
        traceConfig.setXAxisIndex(xAxisIndex);
        traceConfig.setYAxisIndex(yAxisIndex);
        traces.add(traceConfig);
    }

    public int getTraceAmount() {
        return traces.size();
    }

    public TraceConfig getTraceConfig(int index) {
        return traces.get(index);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getMarginColor() {
        return marginColor;
    }

    public void setMarginColor(Color marginColor) {
        this.marginColor = marginColor;
    }

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    public TextStyle getTitleTextStyle() {
        return titleTextStyle;
    }


    public LegendConfig getLegendConfig() {
        return legendConfig;
    }

    public TooltipConfig getTooltipConfig() {
        return tooltipConfig;
    }

    public CrosshairConfig getCrosshairConfig() {
        return crosshairConfig;
    }
}
