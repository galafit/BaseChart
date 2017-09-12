package configuration;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by galafit on 18/8/17.
 */
public class ChartConfig {
    public static final int DEBUG_THEME = 0;
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;

    public String title = "Chart title";
    public Color background;
    public Margin chartMargin = new Margin(10, 10, 10, 10);
    public TextStyle titleTextStyle = new TextStyle();
    public LegendConfig legendConfig = new LegendConfig();
    public TooltipConfig tooltipConfig = new TooltipConfig();
    public CrosshairConfig crosshairConfig = new CrosshairConfig();

    private ArrayList<AxisConfig> xAxisConfigs = new ArrayList<AxisConfig>();
    private ArrayList<AxisConfig> yAxisConfigs = new ArrayList<AxisConfig>();

    public ChartConfig(int theme) {
        titleTextStyle.isBold = true;
        titleTextStyle.fontSize = 14;
        xAxisConfigs.add(new AxisConfig(Orientation.BOTTOM));
        yAxisConfigs.add(new AxisConfig(Orientation.LEFT));
        if(theme == DARK_THEME) {
          background = new Color(20, 20, 30);
          Color textColor = new Color(200, 200, 200);
          legendConfig.background = background;
          legendConfig.borderWidth = 0;
          legendConfig.textStyle.fontColor = textColor;
          titleTextStyle.fontColor = textColor;
        }else if(theme == DEBUG_THEME) {
            background = new Color(20, 20, 30);
            Color textColor = new Color(200, 200, 200);
            legendConfig.background = Color.BLACK;
            legendConfig.borderWidth = 1;
            legendConfig.textStyle.fontColor = textColor;
            titleTextStyle.fontColor = textColor;
        }
    }

    public int getXAxisAmount() {
        return xAxisConfigs.size();
    }

    public int getYAxisAmount() {
        return yAxisConfigs.size();
    }

    public AxisConfig getXAxisConfig(int axisIndex) {
        return xAxisConfigs.get(axisIndex);
    }

    public AxisConfig getYAxisConfig(int axisIndex) {
        return yAxisConfigs.get(axisIndex);
    }

    public void addYAxis(boolean isOpposite) {
        Orientation orientation = isOpposite ? Orientation.RIGHT : Orientation.LEFT;
        AxisConfig axisConfig = new AxisConfig(orientation);
        axisConfig.linesConfig.minorGridLineWidth = 0;
        axisConfig.linesConfig.gridLineWidth = 0;
        yAxisConfigs.add(axisConfig);
    }

}
