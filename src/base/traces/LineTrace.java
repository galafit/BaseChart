package base.traces;

import base.DataSet;
import base.config.traces.LineTraceConfig;
import base.Range;
import base.legend.LegendItem;
import base.tooltips.TooltipItem;
import com.sun.xml.internal.rngom.parse.host.Base;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/9/17.
 */
public class LineTrace extends BaseTrace {

    public LineTrace(LineTraceConfig traceConfig, DataSet dataSet) {
        this.traceConfig = traceConfig;
        setData(dataSet);
    }
}
