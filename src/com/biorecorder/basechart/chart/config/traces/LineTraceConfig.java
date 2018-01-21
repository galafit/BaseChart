package com.biorecorder.basechart.chart.config.traces;

import com.biorecorder.basechart.chart.BColor;
import com.biorecorder.basechart.chart.BStroke;

/**
 * Created by galafit on 11/10/17.
 */
public class LineTraceConfig implements TraceConfig {
    public static final int LINEAR = 0;
    public static final int STEP = 1;
    public static final int PAPA = 2;

    private MarkConfig markConfig = new MarkConfig();
    private BStroke lineStroke = new BStroke();
    private BColor color;
    private int mode;
    private boolean isFilled = false;

    public LineTraceConfig() {
        this(LINEAR, false);
    }

    public LineTraceConfig(boolean isFilled) {
        this(LINEAR, isFilled);
    }

    public LineTraceConfig(int mode) {
      this(mode, false);
    }

    public LineTraceConfig(int mode, boolean isFilled) {
        this.mode = mode;
        this.isFilled = isFilled;
    }

    @Override
    public TraceType getTraceType() {
        return TraceType.LINE;
    }

    public int getMode() {
        return mode;
    }

    public boolean isFilled() {
        return isFilled;
    }

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
