package base.config;

import base.DataSet;
import base.config.axis.AxisConfig;
import base.config.axis.Orientation;
import base.config.general.Margin;
import base.config.general.TextStyle;
import base.config.traces.TraceConfig;
import base.Range;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by galafit on 18/8/17.
 */
public class ChartConfig {
    public static final int DEFAULT_WEIGHT = 10;
    private boolean isTopOpposite = true;
    private boolean isRightOpposite = true;

    private String title = "Chart title";
    private Color background;
    private Color marginColor;
    private Margin margin;
    private TextStyle titleTextStyle = new TextStyle();
    private LegendConfig legendConfig = new LegendConfig();
    private TooltipConfig tooltipConfig = new TooltipConfig();
    private CrosshairConfig crosshairConfig = new CrosshairConfig();
    private ArrayList<Integer> weights = new ArrayList<Integer>();
    /*
     * 2 X-base.axis: 0(even) - BOTTOM and 1(odd) - TOP
     * 2 Y-base.axis for every section(stack): even - LEFT and odd - RIGHT;
     * All LEFT and RIGHT Y-base.axis are stacked.
     * If there is no trace associated with some axis... this axis is invisible.
     **/
    private ArrayList<AxisConfig> xAxisConfigs = new ArrayList<AxisConfig>();
    private ArrayList<AxisConfig> yAxisConfigs = new ArrayList<AxisConfig>();

    private ArrayList<TraceInfo> traces = new ArrayList<TraceInfo>();
    private ArrayList<DataSet> data = new ArrayList<DataSet>();


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

    public void setData(ArrayList<DataSet> data) {
        this.data = data;
    }

    public boolean isTopOpposite() {
        return isTopOpposite;
    }

    public void setTopOpposite(boolean isTopOpposite) {
        this.isTopOpposite = isTopOpposite;
    }

    public boolean isRightOpposite() {
        return isRightOpposite;
    }

    public void setRightOpposite(boolean isLeftOpposite) {
        this.isRightOpposite = isLeftOpposite;
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

    public void setXAxisMinMax(int xAxisIndex, Range minMax) {
        getXAxisConfig(xAxisIndex).setExtremes(minMax.start(), minMax.end());
    }

    public void setYAxisMinMax(int yAxisIndex, Range minMax) {
        getYAxisConfig(yAxisIndex).setExtremes(minMax.start(), minMax.end());
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
    public void addTrace(TraceConfig traceConfig, int dataIndex, String traceName, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        boolean isBottomXAxis = true;
        boolean isLeftYAxis = true;
        if(isXAxisOpposite && isTopOpposite || !isXAxisOpposite && !isTopOpposite) {
            isBottomXAxis = false;
        }
        if(isYAxisOpposite && isRightOpposite || !isYAxisOpposite && !isRightOpposite) {
            isLeftYAxis = false;
        }
        int xAxisIndex = isBottomXAxis ? 0 : 1;
        int yAxisIndex = isLeftYAxis ? yAxisConfigs.size() - 2 : yAxisConfigs.size() - 1;


        xAxisConfigs.get(xAxisIndex).setVisible(true);
        yAxisConfigs.get(yAxisIndex).setVisible(true);
        TraceInfo traceInfo = new TraceInfo();
        String name = (traceName != null) ? traceName : "Trace "+ traces.size();
        traceInfo.setName(name);
        traceInfo.setXAxisIndex(xAxisIndex);
        traceInfo.setYAxisIndex(yAxisIndex);
        traceInfo.setDataIndex(dataIndex);
        traceInfo.setTraceConfig(traceConfig);
        traces.add(traceInfo);
    }



        // add trace to the last stack
    public void addTrace(TraceConfig traceConfig, DataSet dataSet, String traceName, boolean isXAxisOpposite, boolean isYAxisOpposite) {
        data.add(dataSet);
        addTrace(traceConfig, data.size() - 1, traceName, isXAxisOpposite, isXAxisOpposite);
    }

    public int getTraceAmount() {
        return traces.size();
    }

    public TraceConfig getTraceConfig(int index) {
        return traces.get(index).getTraceConfig();
    }

    public DataSet getTraceData(int traceIndex) {
        return data.get(traces.get(traceIndex).getDataIndex());
    }

    public String getTraceName(int traceIndex){
        return traces.get(traceIndex).getName();
    }

    public int getTraceXAxisIndex(int traceIndex){
        return traces.get(traceIndex).getXAxisIndex();
    }

    public int getTraceYAxisIndex(int traceIndex){
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
