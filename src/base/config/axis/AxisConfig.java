package base.config.axis;

import base.config.general.LineConfig;
import base.Range;

import java.awt.*;

/**
 * Created by galafit on 5/9/17.
 */
public class AxisConfig {
    private Orientation orientation;
    private int weight = 1;
    private Color color =  Color.GRAY;

    private boolean isVisible = false;
    private String title = "Title";
    private TitleConfig titleConfig = new TitleConfig();
    private LineConfig axisLineConfig = new LineConfig();
    private LineConfig gridLineConfig = new LineConfig();
    private LineConfig minorGridLineConfig = new LineConfig();
    private int minorGridCounter = 5; // minor grid divider

    private TicksConfig ticksConfig = new TicksConfig();
    private LabelsConfig labelsConfig = new LabelsConfig();

    private boolean isRoundingEnabled = true;
    private boolean isAutoScale = true;
    Range minMax;

    public int getWeight() {
        return weight;
    }

    public AxisConfig(Orientation orientation) {
        this.orientation = orientation;
        getGridLineConfig().setColor(new Color(100, 100, 100));
        getMinorGridLineConfig().setColor(new Color(80, 80, 80));
        getTitleConfig().getTextStyle().setFontSize(14);
        getLabelsConfig().getTextStyle().setFontSize(12);
    }

    public void setExtremes(double min, double max) {
        this.minMax = new Range(min, max);
        setAutoScale(false);
    }

    public Range getExtremes() {
        return minMax;
    }

    public boolean isTop() {
        if(orientation == Orientation.TOP) {
            return true;
        }
        return false;
    }

    public boolean isBottom() {
        if(orientation == Orientation.BOTTOM) {
            return true;
        }
        return false;
    }

    public boolean isLeft() {
        if(orientation == Orientation.LEFT) {
            return true;
        }
        return false;
    }

    public boolean isRight() {
        if(orientation == Orientation.RIGHT) {
            return true;
        }
        return false;
    }

    public boolean isHorizontal() {
        if(orientation == Orientation.BOTTOM || orientation == Orientation.TOP) {
            return true;
        }
        return false;
    }

    public Color getAxisLineColor() {
        return (getAxisLineConfig().getColor() != null) ? getAxisLineConfig().getColor() : getColor();
    }

    public Color getGridColor() {
        return (getGridLineConfig().getColor() != null) ? getGridLineConfig().getColor() : getColor();
    }

    public Color getMinorGridColor() {
        return (getMinorGridLineConfig().getColor() != null) ? getMinorGridLineConfig().getColor() : getColor();
    }

    public Color getLabelsColor() {
        return (getLabelsConfig().getTextStyle().getFontColor() != null) ? getLabelsConfig().getTextStyle().getFontColor() : getColor();
    }

    public Color getTitleColor() {
        return (getTitleConfig().getTextStyle().getFontColor() != null) ? getTitleConfig().getTextStyle().getFontColor() : getColor();
    }

    public Color getTicksColor() {
        return getColor();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TitleConfig getTitleConfig() {
        return titleConfig;
    }

    public LineConfig getAxisLineConfig() {
        return axisLineConfig;
    }

    public LineConfig getGridLineConfig() {
        return gridLineConfig;
    }

    public LineConfig getMinorGridLineConfig() {
        return minorGridLineConfig;
    }

    public int getMinorGridCounter() {
        return minorGridCounter;
    }

    public TicksConfig getTicksConfig() {
        return ticksConfig;
    }

    public LabelsConfig getLabelsConfig() {
        return labelsConfig;
    }


    public boolean isRoundingEnabled() {
        return isRoundingEnabled;
    }

    public void setRoundingEnabled(boolean isRoundingEnabled) {
        this.isRoundingEnabled = isRoundingEnabled;
    }

    public boolean isAutoScale() {
        return isAutoScale;
    }

    public void setAutoScale(boolean isAutoScale) {
        this.isAutoScale = isAutoScale;
    }
}
