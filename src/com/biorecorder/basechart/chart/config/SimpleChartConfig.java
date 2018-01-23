package com.biorecorder.basechart.chart.config;

import com.biorecorder.basechart.chart.*;
import com.biorecorder.basechart.chart.config.traces.TraceConfig;
import com.biorecorder.basechart.chart.RangeInt;

import java.util.*;

/**
 * Created by galafit on 18/8/17.
 */
public class SimpleChartConfig {
    public static final int DEFAULT_WEIGHT = 10;

    private String title;
    private BColor background;
    private BColor marginColor;
    private Margin margin;
    private TextStyle titleTextStyle = new TextStyle(TextStyle.DEFAULT, TextStyle.BOLD, 14);
    private BColor titleColor = BColor.BLACK;
    private LegendConfig legendConfig = new LegendConfig();
    private TooltipConfig tooltipConfig = new TooltipConfig();
    private CrosshairConfig crosshairConfig = new CrosshairConfig();

    private ArrayList<Integer> stackWeights = new ArrayList<Integer>();
    /*
     * 2 X-com.biorecorder.basechart.chart.axis: 0(even) - BOTTOM and 1(odd) - TOP
     * 2 Y-com.biorecorder.basechart.chart.axis for every section(stack): even - LEFT and odd - RIGHT;
     * All LEFT and RIGHT Y-com.biorecorder.basechart.chart.axis are stacked.
     * If there is no trace associated with some axis... this axis is invisible.
     **/
    private ArrayList<AxisConfig> xAxisConfigs = new ArrayList<AxisConfig>();
    private ArrayList<AxisConfig> yAxisConfigs = new ArrayList<AxisConfig>();
    private Map<Integer, Range> xAxisExtremes = new HashMap<Integer, Range>();
    private Map<Integer, Range> yAxisExtremes = new HashMap<Integer, Range>();
    private AxisConfig leftAxisConfig = new AxisConfig(AxisOrientation.LEFT);
    private AxisConfig rightAxisConfig = new AxisConfig(AxisOrientation.RIGHT);
    private boolean isLeftAxisPrimary = true;
    private boolean isBottomAxisPrimary = true;

    private ArrayList<TraceInfo> traces = new ArrayList<TraceInfo>();

    public SimpleChartConfig(boolean isBottomAxisPrimary, boolean isLeftAxisPrimary, AxisConfig leftAxisConfig, AxisConfig rightAxisConfig) {
        this.isLeftAxisPrimary = isLeftAxisPrimary;
        this.isBottomAxisPrimary = isBottomAxisPrimary;
        this.leftAxisConfig = leftAxisConfig;
        this.rightAxisConfig = rightAxisConfig;
        if (leftAxisConfig == null) {
            this.leftAxisConfig = new AxisConfig(AxisOrientation.LEFT);
            if (isLeftAxisPrimary) {
                this.leftAxisConfig.setGridLineStroke(new BStroke(1));
            }
        }
        if (rightAxisConfig == null) {
            this.rightAxisConfig = new AxisConfig(AxisOrientation.RIGHT);
            if (!isLeftAxisPrimary) {
                this.rightAxisConfig.setGridLineStroke(new BStroke(1));
            }
        }

        AxisConfig bottomConfig = new AxisConfig(AxisOrientation.BOTTOM);
        AxisConfig topConfig = new AxisConfig(AxisOrientation.TOP);
        if (isBottomAxisPrimary) {
            bottomConfig.setGridLineStroke(new BStroke(1));
        } else {
            topConfig.setGridLineStroke(new BStroke(1));
        }
        xAxisConfigs.add(bottomConfig);
        xAxisConfigs.add(topConfig);

        addStack(DEFAULT_WEIGHT);
    }

    public SimpleChartConfig(boolean isBottomAxisPrimary, boolean isLeftAxisPrimary) {
        this(isBottomAxisPrimary, isLeftAxisPrimary, null, null);
    }


    public SimpleChartConfig() {
        this( true, true, null, null);
    }

    public void addStack(int weight) {
        AxisConfig leftConfig = new AxisConfig(leftAxisConfig);
        AxisConfig rightConfig = new AxisConfig(rightAxisConfig);
        yAxisConfigs.add(leftConfig);
        yAxisConfigs.add(rightConfig);
        stackWeights.add(weight);
    }

    public int getSumWeight() {
        int weightSum = 0;
        for (Integer weight : stackWeights) {
            weightSum += weight;
        }
        return weightSum;
    }

    public RangeInt getYStartEnd(int yAxisIndex, BRectangle area) {
        int weightSum = getSumWeight();

        int weightSumTillYAxis = 0;
        for (int i = 0; i < yAxisIndex / 2; i++) {
            weightSumTillYAxis += stackWeights.get(i);
        }

        int yAxisWeight = stackWeights.get(yAxisIndex / 2);
        int axisHeight = area.height * yAxisWeight / weightSum;

        int end = area.y + area.height * weightSumTillYAxis / weightSum;
        int start = end + axisHeight;
        return new RangeInt(start, end, true);
    }


    // add trace to the last stack
    public void addTrace(TraceConfig traceConfig, int dataIndex, String traceName, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        boolean isBottomXAxis = true;
        boolean isLeftYAxis = true;
        if (isXAxisOpposite && isBottomAxisPrimary) {
            isBottomXAxis = false;
        }
        if (!isXAxisOpposite && !isBottomAxisPrimary) {
            isBottomXAxis = false;
        }
        if (isYAxisOpposite && isLeftAxisPrimary) {
            isLeftYAxis = false;
        }
        if (!isYAxisOpposite && !isLeftAxisPrimary) {
            isLeftYAxis = false;
        }
        int xAxisIndex = isBottomXAxis ? 0 : 1;
        int yAxisIndex = isLeftYAxis ? yAxisConfigs.size() - 2 : yAxisConfigs.size() - 1;

        xAxisConfigs.get(xAxisIndex).setVisible(true);
        yAxisConfigs.get(yAxisIndex).setVisible(true);
        TraceInfo traceInfo = new TraceInfo();
        String name = (traceName != null) ? traceName : "Trace " + traces.size();
        traceInfo.setName(name);
        traceInfo.setXAxisIndex(xAxisIndex);
        traceInfo.setYAxisIndex(yAxisIndex);
        traceInfo.setDataIndex(dataIndex);
        traceInfo.setTraceConfig(traceConfig);
        traces.add(traceInfo);
    }

    public BColor getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(BColor titleColor) {
        this.titleColor = titleColor;
    }

    public int getNumberOfXAxis() {
        return xAxisConfigs.size();
    }

    public int getNumberOfYAxis() {
        return yAxisConfigs.size();
    }

    public int getTraceCounter() {
        return traces.size();
    }

    public AxisConfig getXConfig(int axisIndex) {
        return xAxisConfigs.get(axisIndex);
    }

    public AxisConfig getYConfig(int axisIndex) {
        return yAxisConfigs.get(axisIndex);
    }

    public void setXMinMax(int xAxisIndex, Range minMax) {
        xAxisExtremes.put(xAxisIndex, minMax);
    }

    public void setYMinMax(int yAxisIndex, Range minMax) {
        yAxisExtremes.put(yAxisIndex, minMax);
    }

    public Range getXMinMax(int xAxisIndex) {
        return xAxisExtremes.get(xAxisIndex);
    }

    public Range getYMinMax(int xAxisIndex) {
        return yAxisExtremes.get(xAxisIndex);
    }

    public TraceConfig getTraceConfig(int index) {
        return traces.get(index).getTraceConfig();
    }

    public String getTraceName(int traceIndex) {
        return traces.get(traceIndex).getName();
    }

    public int getTraceXIndex(int traceIndex) {
        return traces.get(traceIndex).getXAxisIndex();
    }

    public int getTraceYIndex(int traceIndex) {
        return traces.get(traceIndex).getYAxisIndex();
    }

    public int getTraceDataIndex(int traceIndex) {
        return traces.get(traceIndex).getDataIndex();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BColor getBackground() {
        return background;
    }

    public void setBackground(BColor background) {
        this.background = background;
    }

    public BColor getMarginColor() {
        return marginColor;
    }

    public void setMarginColor(BColor marginColor) {
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

    class TraceInfo {
        private TraceConfig traceConfig;
        private int dataIndex;
        private int xAxisIndex;
        private int yAxisIndex;
        private String name;

        public TraceConfig getTraceConfig() {
            return traceConfig;
        }

        public void setTraceConfig(TraceConfig traceConfig) {
            this.traceConfig = traceConfig;
        }

        public int getDataIndex() {
            return dataIndex;
        }

        public void setDataIndex(int dataIndex) {
            this.dataIndex = dataIndex;
        }

        public int getXAxisIndex() {
            return xAxisIndex;
        }

        public void setXAxisIndex(int xAxisIndex) {
            this.xAxisIndex = xAxisIndex;
        }

        public int getYAxisIndex() {
            return yAxisIndex;
        }

        public void setYAxisIndex(int yAxisIndex) {
            this.yAxisIndex = yAxisIndex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}