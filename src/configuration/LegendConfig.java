package configuration;

import java.awt.*;

/**
 * Created by galafit on 18/8/17.
 */
public class LegendConfig {
    private TextStyle textStyle = new TextStyle();
    private Position position = Position.TOP_CENTER;
    private Color background = Color.WHITE;
    private Color borderColor = Color.LIGHT_GRAY;
    private int borderWidth = 0;
    private Padding padding;

    public TextStyle getTextStyle() {
        return textStyle;
    }
    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
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

    public Padding getPadding(){
        if(padding != null) {
            return padding;
        }
        return new Padding((int)(getTextStyle().getFontSize() * 0),
                (int)(getTextStyle().getFontSize() * 1),
                (int)(getTextStyle().getFontSize() * 0.5),
                (int)(getTextStyle().getFontSize() * 1));
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }
}
