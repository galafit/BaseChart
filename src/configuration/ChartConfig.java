package configuration;

import configuration.axis.AxisConfig;
import configuration.axis.Orientation;
import configuration.general.Margin;
import configuration.general.TextStyle;
import data.Range;

import java.awt.*;
import java.util.*;

/**
 * Created by galafit on 18/8/17.
 */
public class ChartConfig {
    public static final int DEBUG_THEME = 0;
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;
    private static final int DEFAULT_WEIGHT = 10;

    public String title = "Chart title";
    public Color background;
    public Color marginColor;
    public Margin margin;
    private Rectangle area;
    public TextStyle titleTextStyle = new TextStyle();
    public LegendConfig legendConfig = new LegendConfig();
    public TooltipConfig tooltipConfig = new TooltipConfig();
    public CrosshairConfig crosshairConfig = new CrosshairConfig();
    private ArrayList<TraceConfig> traces = new ArrayList<TraceConfig>();
    private ArrayList<Integer> weights = new ArrayList<Integer>();
    /*
     * 2 X-axis: 0(even) - BOTTOM and 1(odd) - TOP
     * 2 Y-axis for every section(stack): even - LEFT and odd - RIGHT;
     * All LEFT and RIGHT Y-axis are stacked.
     * If there is no trace associated with the axis... axis is invisible.
     **/
    private ArrayList<AxisConfig> xAxisConfigs = new ArrayList<AxisConfig>();
    private ArrayList<AxisConfig> yAxisConfigs = new ArrayList<AxisConfig>();

    public ChartConfig(int theme, int width, int height) {
        this(theme, new Rectangle(0, 0, width, height));
    }

    public ChartConfig(int theme, Rectangle area) {
        titleTextStyle.isBold = true;
        titleTextStyle.fontSize = 14;
        this.area = area;
        weights.add(DEFAULT_WEIGHT);

        AxisConfig axisConfig = new AxisConfig(Orientation.BOTTOM);
        axisConfig.minorGridLineConfig.width = 0;
        axisConfig.gridLineConfig.width = 1;
        xAxisConfigs.add(axisConfig);

        axisConfig = new AxisConfig(Orientation.TOP);
        axisConfig.minorGridLineConfig.width = 0;
        axisConfig.gridLineConfig.width = 0;
        xAxisConfigs.add(axisConfig);

        axisConfig = new AxisConfig(Orientation.LEFT);
        axisConfig.minorGridLineConfig.width = 0;
        axisConfig.gridLineConfig.width = 1;
        yAxisConfigs.add(axisConfig);

        axisConfig = new AxisConfig(Orientation.RIGHT);
        axisConfig.minorGridLineConfig.width = 0;
        axisConfig.gridLineConfig.width = 0;
        yAxisConfigs.add(axisConfig);

        if(theme == DARK_THEME) {
          background = new Color(20, 20, 30);
          marginColor = background;
          Color textColor = new Color(200, 200, 200);
          legendConfig.background = background;
          legendConfig.borderWidth = 0;
          legendConfig.textStyle.fontColor = textColor;
          titleTextStyle.fontColor = textColor;
        }else if(theme == DEBUG_THEME) {
            background = new Color(20, 20, 30);
            marginColor = new Color(20, 20, 20);;
            Color textColor = new Color(200, 200, 200);
            legendConfig.background = Color.BLACK;
            legendConfig.borderWidth = 1;
            legendConfig.textStyle.fontColor = textColor;
            titleTextStyle.fontColor = textColor;
        }
    }

    public Rectangle getArea() {
        return area;
    }

    public void setArea(Rectangle area) {
        this.area = area;
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

    public void addStack(int weight) {
        AxisConfig axisConfig = new AxisConfig(Orientation.LEFT);
        axisConfig.minorGridLineConfig.width = 0;
        axisConfig.gridLineConfig.width = 1;
        yAxisConfigs.add(axisConfig);

        axisConfig = new AxisConfig(Orientation.RIGHT);
        axisConfig.minorGridLineConfig.width = 0;
        axisConfig.gridLineConfig.width = 0;
        yAxisConfigs.add(axisConfig);
        weights.add(weight);
    }

    public void addStack() {
        addStack(DEFAULT_WEIGHT);
    }


    public Range getYAxisStartEnd(int yAxisIndex, Rectangle area) {
        int weightSum = 0;
        for (Integer weight : weights) {
            weightSum += weight;
        }

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



    // add trace to the last stack
    public void addTrace(TraceConfig traceConfig, boolean isBottomXAxis, boolean isLeftYAxis) {
        int xAxisIndex = isBottomXAxis ? 0 : 1;
        int yAxisIndex = isLeftYAxis ? yAxisConfigs.size() - 2 : yAxisConfigs.size() - 1;
        xAxisConfigs.get(xAxisIndex).isVisible = true;
        yAxisConfigs.get(yAxisIndex).isVisible = true;
        traceConfig.xAxisIndex = xAxisIndex;
        traceConfig.yAxisIndex = yAxisIndex;
        traces.add(traceConfig);
    }

    public int getTraceAmount() {
        return traces.size();
    }

    public TraceConfig getTraceConfig(int index) {
        return traces.get(index);
    }
}
