package com.biorecorder.basechart.chart.traces;

import com.biorecorder.basechart.chart.*;
import com.biorecorder.basechart.chart.tooltip.InfoItem;


/**
 * Created by galafit on 16/9/17.
 */
public abstract class Trace {
    private Axis xAxis;
    private Axis yAxis;
    private String name;
    private BColor defaultColor;
    private DataSet data;

    public void setData(DataSet data) {
        this.data = data;
    }

    public DataSet getData() {
        return data;
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public void setXAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    public void setYAxis(Axis yAxis) {
        this.yAxis = yAxis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BColor getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(BColor defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Range getXExtremes() {
        return data.getXExtremes();
    }

    public abstract Range getYExtremes();

    public abstract BPoint getDataPosition(int dataIndex);

    public abstract InfoItem[] getInfo(int dataIndex);

    public abstract void draw(BCanvas canvas);
}
