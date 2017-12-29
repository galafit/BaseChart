package base.scales;

import base.config.LabelFormatInfo;

public abstract class Scale {
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

    public abstract TickProvider getTickProvider(int tickCount, LabelFormatInfo labelFormatInfo);

    public abstract TickProvider getTickProvider(double tickStep, Unit tickUnit, LabelFormatInfo labelFormatInfo);

}

