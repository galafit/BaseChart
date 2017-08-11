package legends;

import java.awt.*;

/**
 * Created by hdablin on 11.08.17.
 */
public class LegendItem {
    private Color color;
    private String label;

    public LegendItem(Color color, String label) {
        this.color = color;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }
}
