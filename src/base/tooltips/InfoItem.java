package base.tooltips;

import java.awt.*;

/**
 * Created by galafit on 6/8/17.
 */
public class InfoItem {
    private String label;
    private String value;
    private Color  markColor;

    public InfoItem(String label, String value, Color markColor) {
        this.label = label;
        this.value = value;
        this.markColor = markColor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Color getMarkColor() {
        return markColor;
    }

    public void setMarkColor(Color markColor) {
        this.markColor = markColor;
    }
}
