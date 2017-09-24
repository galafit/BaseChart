package configuration.general;

import javax.swing.*;
import java.awt.*;

/**
 * Created by galafit on 18/8/17.
 */
public class TextStyle {
    public int fontSize = 12;
    public Color fontColor;
    // Logical fonts: Serif, SansSerif, Monospaced, Dialog, and DialogInput.
    public String fontName; // Font.SANS_SERIF;
    public boolean isBold = false;
    public boolean isItalic = false;

    public Font getFont() {
        int style;
        String name = (fontName != null) ? fontName: new JLabel().getFont().getFontName();
        if(isBold && isItalic){
            style = Font.BOLD + Font.ITALIC;
        } else if(isBold){
            style = Font.BOLD;
        } else if(isItalic){
            style = Font.ITALIC;
        } else {
            style = Font.PLAIN;
        }
        return new Font(name, style, fontSize);
    }
}
