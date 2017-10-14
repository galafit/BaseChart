package base.config.traces;

import base.config.general.LineConfig;
import base.config.general.MarkConfig;
import base.traces.XYMapper;

import java.awt.*;

/**
 * Created by galafit on 11/10/17.
 */
public abstract class BaseTraceConfig implements TraceConfig {
    private int hoverExtraSize = 3; //px
    private Color hoverColor;
    private MarkConfig markConfig = new MarkConfig();
    private LineConfig lineConfig = new LineConfig();
    private XYMapper mapper = new XYMapper();

    public XYMapper getMapper() {
        return mapper;
    }

    public void setMapper(XYMapper mapper) {
        this.mapper = mapper;
    }

    public Color getHoverColor() {
        return hoverColor;
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
