package configuration.traces;

import configuration.general.LineConfig;
import configuration.general.MarkConfig;
import data.datasets.XYData;

import java.awt.*;

/**
 * Created by galafit on 30/9/17.
 */
public class LineTraceConfig extends TraceConfig {
    private int hoverExtraSize = 3; //px
    private Color hoverColor;
    private MarkConfig markConfig = new MarkConfig();
    private LineConfig lineConfig = new LineConfig();
    private XYData data;

    public LineTraceConfig(XYData data) {
        this.data = data;
    }

    public TraceType getTraceType() {
        return TraceType.LINE;
    }

    public XYData getData() {
        return data;
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        if(markConfig.color == null) {
            markConfig.color = color;
        }
        if(lineConfig.color == null) {
            lineConfig.color = color;
        }
    }

    public Color getHoverColor() {
        return (hoverColor != null) ? hoverColor : getColor().brighter();
    }

    public int getHoverSize() {
        return Math.max(lineConfig.width, markConfig.size) + hoverExtraSize;
    }

    public LineConfig getLineConfig() {
        return lineConfig;
    }

    public MarkConfig getMarkConfig() {
        return markConfig;
    }
}
