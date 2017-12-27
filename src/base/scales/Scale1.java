package base.scales;

import base.config.axis.LabelFormatInfo;
import java.util.List;

public abstract class Scale1 {
    protected final int DEFAULT_TICKS_AMOUNT = 10;
    protected double domain[] = {0, 1};
    protected double range[] = {0, 1};

    public void setDomain(double... domain) {
        this.domain = domain;
    }

    public void setRange(double... range) {
        this.range = range;
    }


    public double[] getDomain() {
        return domain;
    }

    public double[] getRange() {
        return range;
    }

    public abstract double scale(double value);

    public abstract double invert(double value);

    public abstract List<Tick> getTicksByStep(double tickStep, LabelFormatInfo labelFormatInfo);

    public abstract List<Tick> getTicksByCount(int tickCount, LabelFormatInfo labelFormatInfo);

    public abstract List<Tick> getTicksByMaxCount(int tickMaxCount, LabelFormatInfo labelFormatInfo);

}

