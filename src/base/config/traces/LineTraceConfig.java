package base.config.traces;

import base.traces.XYMapper;
import base.config.general.LineConfig;
import base.config.general.MarkConfig;

import java.awt.*;

/**
 * Created by galafit on 30/9/17.
 */
public class LineTraceConfig extends BaseTraceConfig {
    @Override
    public TraceType getTraceType() {
        return TraceType.LINE;
    }
}
