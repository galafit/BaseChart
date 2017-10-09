package base.config.traces;

import base.XYData;
import base.config.general.LineConfig;
import base.config.general.MarkConfig;

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
    public void setData(Object data) {
        this.data = (XYData) data;
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        if(markConfig.getColor() == null) {
            markConfig.setColor(color);
        }
        if(lineConfig.getColor() == null) {
            lineConfig.setColor(color);
        }
    }

    public Color getHoverColor() {
        return (hoverColor != null) ? hoverColor : getColor().brighter();
    }

    public int getHoverSize() {
        return Math.max(lineConfig.getWidth(), markConfig.getSize()) + hoverExtraSize;
    }

    public LineConfig getLineConfig() {
        return lineConfig;
    }

    public MarkConfig getMarkConfig() {
        return markConfig;
    }
}
