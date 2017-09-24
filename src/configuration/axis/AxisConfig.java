package configuration.axis;

import configuration.general.LineConfig;
import java.awt.*;

/**
 * Created by galafit on 5/9/17.
 */
public class AxisConfig {
    private Orientation orientation;
    private int weight = 1;
    public Color color =  Color.GRAY;

    public boolean isVisible = false;
    public String title = "Title";
    public TitleConfig titleConfig = new TitleConfig();
    public LineConfig axisLineConfig = new LineConfig();
    public LineConfig gridLineConfig = new LineConfig();
    public LineConfig minorGridLineConfig = new LineConfig();
    public int minorGridCounter = 5; // minor grid divider

    public TicksConfig ticksConfig = new TicksConfig();
    public LabelsConfig labelsConfig = new LabelsConfig();

    public boolean isRoundingEnabled = true;
    public boolean isAutoScale = true;

    public int getWeight() {
        return weight;
    }

    public AxisConfig(Orientation orientation) {
        this.orientation = orientation;
        gridLineConfig.color = new Color(100, 100, 100);
        minorGridLineConfig.color = new Color(80, 80, 80);
        titleConfig.textStyle.fontSize = 14;
        labelsConfig.textStyle.fontSize = 12;
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
        return (axisLineConfig.color != null) ? axisLineConfig.color : color;
    }

    public Color getGridColor() {
        return (gridLineConfig.color != null) ? gridLineConfig.color : color;
    }

    public Color getMinorGridColor() {
        return (minorGridLineConfig.color != null) ? minorGridLineConfig.color : color;
    }

    public Color getLabelsColor() {
        return (labelsConfig.textStyle.fontColor != null) ? labelsConfig.textStyle.fontColor : color;
    }

    public Color getTitleColor() {
        return (titleConfig.textStyle.fontColor != null) ? titleConfig.textStyle.fontColor : color;
    }

    public Color getTicksColor() {
        return color;
    }

}
