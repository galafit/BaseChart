package configuration.traces;

import data.datasets.XYData;

import java.awt.*;

/**
 * Created by galafit on 30/9/17.
 */
public class AreaTraceConfig extends LineTraceConfig {
    public AreaTraceConfig(XYData data) {
        super(data);
    }

    @Override
    public TraceType getTraceType() {
        return TraceType.AREA;
    }

    public Color getFillColor() {
        return new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), 90);
    }
}
