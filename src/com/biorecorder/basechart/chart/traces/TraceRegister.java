package com.biorecorder.basechart.chart.traces;

import com.biorecorder.basechart.chart.DataSet;
import com.biorecorder.basechart.chart.config.traces.LineTraceConfig;
import com.biorecorder.basechart.chart.config.traces.TraceConfig;

/**
 * Created by galafit on 30/9/17.
 */
public class TraceRegister {
    public static Trace getTrace(TraceConfig traceConfig, DataSet data) {
        switch(traceConfig.getTraceType())
        {
            case LINE:
                return new LineTrace((LineTraceConfig) traceConfig, data);
        }
        return null;
    }
}
