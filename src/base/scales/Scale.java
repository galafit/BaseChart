package base.scales;

import base.config.LabelFormatInfo;

public abstract class Scale {
    protected double domain[] = {0, 1};
    protected float range[] = {0, 1};

    public void setDomain(double... domain) {
        this.domain = domain;
    }

    public void setRange(float... range) {
        this.range = range;
    }


    public double[] getDomain() {
        return domain;
    }

    public float[] getRange() {
        return range;
    }

    public abstract float scale(double value);

    public abstract double invert(float value);

    public abstract TickProvider getTickProvider(int tickCount, LabelFormatInfo labelFormatInfo);

    public abstract TickProvider getTickProvider(double tickStep, Unit tickUnit, LabelFormatInfo labelFormatInfo);

}

