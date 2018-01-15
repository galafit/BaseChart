package com.biorecorder.basechart.chart.config.traces;

import com.biorecorder.basechart.chart.BColor;
import com.biorecorder.basechart.chart.BStroke;

/**
 * Created by galafit on 11/10/17.
 */
public abstract class BaseTraceConfig implements TraceConfig {
    private MarkConfig markConfig = new MarkConfig();
    private BStroke lineStroke = new BStroke();
    private BColor color;

    public MarkConfig getMarkConfig() {
        return markConfig;
    }

    public void setMarkConfig(MarkConfig markConfig) {
        this.markConfig = markConfig;
    }

    public BStroke getLineStroke() {
        return lineStroke;
    }

    public void setLineStroke(BStroke lineStroke) {
        this.lineStroke = lineStroke;
    }

    public BColor getColor() {
        return color;
    }

    public void setColor(BColor color) {
        this.color = color;
    }
}
