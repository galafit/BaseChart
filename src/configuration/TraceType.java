package configuration;

import traces.AreaTrace;
import traces.LineTrace;
import traces.Trace;

/**
 * Created by galafit on 14/9/17.
 */
public enum TraceType {
    LINE {
        @Override
        public Trace getTrace() {
            return new LineTrace();
        }
    },
    AREA {
        @Override
        public Trace getTrace() {
            return new AreaTrace();
        }
    };

    public abstract Trace getTrace();
}
