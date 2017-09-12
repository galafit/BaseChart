package configuration;

import java.awt.*;

/**
 * Created by galafit on 5/9/17.
 */
public class AxisConfig {
    private Orientation orientation;
    public boolean isAxisVisible = true;

    public String name = "Name";
    public TextStyle nameTextStyle = new TextStyle();
    public int namePadding = 0;// (int)(0.8 * nameTextStyle.fontSize);
    public boolean isNameVisible = true;

    public LinesConfig linesConfig = new LinesConfig();
    public TicksConfig ticksConfig = new TicksConfig();

    public boolean isRoundingEnabled = true;
    public boolean isAutoScale = true;

    public AxisConfig(Orientation orientation) {
        this.orientation = orientation;
        nameTextStyle.fontSize = 20;
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

}
