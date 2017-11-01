package base.traces;

import base.DataSet;
import base.config.traces.LineTraceConfig;

/**
 * Created by galafit on 20/9/17.
 */
public class LineTrace extends BaseTrace {

    public LineTrace(LineTraceConfig traceConfig, DataSet dataSet) {
        this.traceConfig = traceConfig;
        setData(dataSet);
    }
}
