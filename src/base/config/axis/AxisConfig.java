package base.config.axis;

import base.config.general.LineConfig;
import java.awt.*;

/**
 * Created by galafit on 5/9/17.
 */
public class AxisConfig {
    private AxisOrientation orientation;
    private Color color =  Color.GRAY;

    private boolean isVisible = false;
    private TitleConfig titleConfig = new TitleConfig();
    private LineConfig axisLineConfig = new LineConfig();
    private LineConfig gridLineConfig = new LineConfig();
    private LineConfig minorGridLineConfig = new LineConfig();
    private int minorGridCounter = 5; // minor grid divider

    private TicksConfig ticksConfig = new TicksConfig();
    private LabelsConfig labelsConfig = new LabelsConfig();
    private String title;
    private boolean isMinMaxRoundingEnable = false;

    public AxisConfig(AxisOrientation orientation) {
        this.orientation = orientation;
        getGridLineConfig().setWidth(1);
        getGridLineConfig().setColor(new Color(100, 100, 100));
        getMinorGridLineConfig().setWidth(0);
        getMinorGridLineConfig().setColor(new Color(80, 80, 80));
        getTitleConfig().getTextStyle().setFontSize(14);
        getLabelsConfig().getTextStyle().setFontSize(12);
    }

    public boolean isMinMaxRoundingEnable() {
        return isMinMaxRoundingEnable;
    }

    public void setMinMaxRoundingEnable(boolean isMinMaxRoundingEvalable) {
        this.isMinMaxRoundingEnable = isMinMaxRoundingEvalable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTop() {
        if(orientation == AxisOrientation.TOP) {
            return true;
        }
        return false;
    }

    public boolean isBottom() {
        if(orientation == AxisOrientation.BOTTOM) {
            return true;
        }
        return false;
    }

    public boolean isLeft() {
        if(orientation == AxisOrientation.LEFT) {
            return true;
        }
        return false;
    }

    public boolean isRight() {
        if(orientation == AxisOrientation.RIGHT) {
            return true;
        }
        return false;
    }

    public boolean isHorizontal() {
        if(orientation == AxisOrientation.BOTTOM || orientation == AxisOrientation.TOP) {
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
}
