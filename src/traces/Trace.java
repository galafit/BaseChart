package traces;

import axis.Axis;
import configuration.traces.TraceConfig;
import data.Range;
import legend.LegendItem;

import java.awt.*;

/**
 * Created by galafit on 16/9/17.
 */
public abstract class Trace {

    public int getXAxisIndex() {
        return getTraceConfig().getXAxisIndex();
    }

    public int getYAxisIndex() {
        return getTraceConfig().getYAxisIndex();
    }

    public LegendItem[] getLegendItems() {
        LegendItem[] items = {new LegendItem(getTraceConfig().getColor(), getTraceConfig().getName())};
        return items;
     }

    abstract TraceConfig getTraceConfig();

    public abstract Range getXExtremes();

    public abstract Range getYExtremes();

    public abstract void draw(Graphics2D g, Axis xAxis, Axis yAxis);
}
