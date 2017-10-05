package configuration.traces;

import data.Data;

import java.awt.*;

/**
 * Created by galafit on 13/9/17.
 */
public abstract class TraceConfig {
    private Color color;
    private String name;
    private int xAxisIndex;
    private int yAxisIndex;

    public abstract TraceType getTraceType();

    public abstract Data getData();

    public int getXAxisIndex() {
        return xAxisIndex;
    }

    public void setXAxisIndex(int xAxisIndex) {
        this.xAxisIndex = xAxisIndex;
    }

    public int getYAxisIndex() {
        return yAxisIndex;
    }

    public void setYAxisIndex(int yAxisIndex) {
        this.yAxisIndex = yAxisIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

 }
