package com.biorecorder.basechart.chart.traces;

import com.biorecorder.basechart.chart.DataSet;
import com.biorecorder.basechart.chart.config.traces.LineTraceConfig;

/**
 * Created by galafit on 20/9/17.
 */
public class LineTrace extends BaseTrace {

    public LineTrace(LineTraceConfig traceConfig, DataSet dataSet) {
        this.traceConfig = traceConfig;
        setData(dataSet);
    }
}
