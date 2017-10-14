package base.traces;

import base.DataSet;
import base.config.traces.AreaTraceConfig;
import base.config.traces.LineTraceConfig;
import base.config.traces.TraceConfig;

/**
 * Created by galafit on 30/9/17.
 */
public class TraceRegister {
    public static Trace getTrace(TraceConfig traceConfig, DataSet data) {
        switch(traceConfig.getTraceType())
        {
            case LINE:
                return new LineTrace((LineTraceConfig) traceConfig, data);
            case AREA:
                return new AreaTrace((AreaTraceConfig) traceConfig, data);
        }
        return null;
    }
}
