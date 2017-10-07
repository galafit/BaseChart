package base.config;

import base.config.general.Margin;
import base.config.general.TextStyle;

import java.awt.*;

/**
 * Created by galafit on 19/8/17.
 */
public class TooltipConfig {
    private TextStyle textStyle = new TextStyle(Color.BLACK);
    private Color background = new Color(220, 220, 220);
    private Color borderColor = new Color(100, 100, 100);
    private int borderWidth = 1;
    private Margin margin = new Margin((int)(getTextStyle().getFontSize() * 0.4),
            (int)(getTextStyle().getFontSize() * 0.8),
            (int)(getTextStyle().getFontSize() * 0.4),
            (int)(getTextStyle().getFontSize() * 0.8));

    public TextStyle getTextStyle() {
        return textStyle;
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
