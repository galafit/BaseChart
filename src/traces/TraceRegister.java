package traces;

import configuration.traces.AreaTraceConfig;
import configuration.traces.LineTraceConfig;
import configuration.traces.TraceConfig;

/**
 * Created by galafit on 30/9/17.
 */
public class TraceRegister {
    public static Trace getTrace(TraceConfig traceConfig) {
        switch(traceConfig.getTraceType())
        {
            case LINE:
                return new LineTrace((LineTraceConfig) traceConfig);
            case AREA:
                return new AreaTrace((AreaTraceConfig) traceConfig);
        }
        return null;
    }
}
