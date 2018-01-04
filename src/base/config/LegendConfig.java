package base.config;

import base.BColor;
import base.Margin;
import base.TextStyle;


/**
 * Created by galafit on 18/8/17.
 */
public class LegendConfig {
    private boolean isVisible = true;
    private TextStyle textStyle = new TextStyle(TextStyle.DEFAULT, TextStyle.NORMAL, 12);
    private LegendPosition position = LegendPosition.TOP_LEFT;

    private int borderWidth = 0;
    private BColor borderColor = BColor.LIGHT_GRAY;
    private BColor backgroundColor = BColor.WHITE;
    private BColor textColor = BColor.BLACK;
    private Margin margin = new Margin((int)(getTextStyle().getSize() * 0),
            (int)(getTextStyle().getSize() * 1),
            (int)(getTextStyle().getSize() * 0.5),
            (int)(getTextStyle().getSize() * 1));

    public BColor getTextColor() {
        return textColor;
    }

    public void setTextColor(BColor textColor) {
        this.textColor = textColor;
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

    public LegendPosition getPosition() {
        return position;
    }

    public void setPosition(LegendPosition position) {
        this.position = position;
    }

    public BColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(BColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public BColor getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(BColor borderColor) {
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
