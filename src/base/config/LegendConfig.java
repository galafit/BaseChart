package base.config;

import base.config.general.Margin;
import base.config.general.TextStyle;

import java.awt.*;

/**
 * Created by galafit on 18/8/17.
 */
public class LegendConfig {
    private boolean isVisible = true;
    private TextStyle textStyle = new TextStyle();
    private Position position = Position.BOTTOM_LEFT;
    private Color background = Color.WHITE;
    private Color borderColor = Color.LIGHT_GRAY;
    private int borderWidth = 0;
    private Margin margin = new Margin((int)(getTextStyle().getFontSize() * 0),
            (int)(getTextStyle().getFontSize() * 1),
            (int)(getTextStyle().getFontSize() * 0.5),
            (int)(getTextStyle().getFontSize() * 1));

    public boolean isTop() {
        return getPosition().isTop();
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        isVisible = isVisible;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }
}
