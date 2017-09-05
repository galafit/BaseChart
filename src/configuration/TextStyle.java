package configuration;

import java.awt.*;

/**
 * Created by galafit on 18/8/17.
 */
public class TextStyle {
    private int fontSize = 12;
    private Color fontColor = Color.BLACK;
    // Logical fonts: Serif, SansSerif, Monospaced, Dialog, and DialogInput.
    private String fontName = Font.SANS_SERIF;
    boolean isBold = false;
    boolean isItalic = false;

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
    }

    public Font getFont() {
        int style;

        if(isBold && isItalic){
            style = Font.BOLD + Font.ITALIC;
        } else if(isBold){
            style = Font.BOLD;
        } else if(isItalic){
            style = Font.ITALIC;
        } else {
            style = Font.PLAIN;
        }
        return new Font(fontName, style, fontSize);
    }
}
