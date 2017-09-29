package configuration;

import configuration.general.LineConfig;
import configuration.general.MarkConfig;
import data.datasets.DataSet;

import java.awt.*;

/**
 * Created by galafit on 13/9/17.
 */
public class TraceConfig {
    private TraceType type;
    private int hoverExtraSize = 3; //px
    private DataSet data;

    public Color color;
    public Color hoverColor;
    public String name;
    public LineConfig lineConfig = new LineConfig();
    public MarkConfig markConfig = new MarkConfig();
    public int xAxisIndex;
    public int yAxisIndex;

    public TraceConfig(TraceType type, DataSet data) {
        this.type = type;
        markConfig.size = 6;
        this.data = data;
    }

    public DataSet getData() {
        return data;
    }

    public boolean isMarksVisible() {
        return (markConfig.size > 0) ? true: false;
    }

    public  boolean isLinesVisible() {
        return (lineConfig.width > 0) ? true: false;
    }

    public TraceType getType() {
        return type;
    }

    public Color getLineColor() {
        return (lineConfig.color != null) ? lineConfig.color : color;
    }

    public Color getMarkColor() {
        return (markConfig.color != null) ? markConfig.color : color;
    }

    public Color getHoverColor() {
        return (hoverColor != null) ? hoverColor : color.brighter();
    }

    public Color getFillColor() {
        return new Color(getMarkColor().getRed(), getMarkColor().getGreen(), getMarkColor().getBlue(), 80);
    }

    public int getHoverSize() {
        return Math.max(lineConfig.width, markConfig.size) + hoverExtraSize;
    }

}
