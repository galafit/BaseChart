package traces;

import axis.Axis;
import configuration.TraceConfig;
import data.Range;

import java.awt.*;

/**
 * Created by galafit on 16/9/17.
 */
public abstract class Trace {
    TraceConfig traceConfig;
    int hoverIndex = -1;

    public abstract void setConfig(TraceConfig traceConfig);

    public int getXAxisIndex() {
        return traceConfig.xAxisIndex;
    }

    public int getYAxisIndex() {
        return traceConfig.yAxisIndex;
    }

    public Range getXRange() {
        return traceConfig.getData().getXRange();
    }

    public Range getYRange() {
        return traceConfig.getData().getYRange();
    }

   public abstract void draw(Graphics2D g, Axis xAxis, Axis yAxis);


}
