package configuration;

import java.awt.*;

/**
 * Created by galafit on 18/8/17.
 */
public class ChartConfig {
    private static final int DEBUG_THEME = 0;
    private static final int DARK_THEME = 1;
    private static final int LIGHT_THEME = 2;

    private String title = "Chart title";
    private Color background;
    private Padding chartPadding = new Padding(10, 10, 10, 10);
    private TextStyle titleTextStyle = new TextStyle();
    private LegendConfig legendConfig = new LegendConfig();
    private TooltipConfig tooltipConfig = new TooltipConfig();
    private CrosshairConfig crosshairConfig = new CrosshairConfig();

    public ChartConfig(int theme) {
        titleTextStyle.setBold(true);
        titleTextStyle.setFontSize(14);
        if(theme == DARK_THEME) {
          background = new Color(20, 20, 30);
          Color textColor = new Color(200, 200, 200);
          legendConfig.setBackground(background);
          legendConfig.setBorderWidth(0);
          legendConfig.getTextStyle().setFontColor(textColor);
          titleTextStyle.setFontColor(textColor);
        }else if(theme == DEBUG_THEME) {
            background = new Color(20, 20, 30);
            Color textColor = new Color(200, 200, 200);
            legendConfig.setBackground(Color.BLACK);
            legendConfig.setBorderWidth(1);
            legendConfig.getTextStyle().setFontColor(textColor);
            titleTextStyle.setFontColor(textColor);
        }
    }

    public ChartConfig() {
        this(DEBUG_THEME);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Padding getChartPadding() {
        return chartPadding;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public void setChartPadding(Padding chartPadding) {
        this.chartPadding = chartPadding;
    }

    public TextStyle getTitleTextStyle() {
        return titleTextStyle;
    }

    public void setTitleTextStyle(TextStyle titleTextStyle) {
        this.titleTextStyle = titleTextStyle;
    }

    public LegendConfig getLegendConfig() {
        return legendConfig;
    }

    public void setLegendConfig(LegendConfig legendConfig) {
        this.legendConfig = legendConfig;
    }

    public TooltipConfig getTooltipConfig() {
        return tooltipConfig;
    }

    public void setTooltipConfig(TooltipConfig tooltipConfig) {
        this.tooltipConfig = tooltipConfig;
    }

    public CrosshairConfig getCrosshairConfig() {
        return crosshairConfig;
    }

    public void setCrosshairConfig(CrosshairConfig crosshairConfig) {
        this.crosshairConfig = crosshairConfig;
    }
}
