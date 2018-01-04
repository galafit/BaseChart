package base.scales;

import base.config.LabelFormatInfo;

public abstract class Scale {
    protected float domain[] = {0, 1};
    protected float range[] = {0, 1};

    public void setDomain(float... domain) {
        this.domain = domain;
    }

    public void setRange(float... range) {
        this.range = range;
    }


    public float[] getDomain() {
        return domain;
    }

    public float[] getRange() {
        return range;
    }

    public abstract float scale(float value);

    public abstract float invert(float value);

    public abstract TickProvider getTickProvider(int tickCount, LabelFormatInfo labelFormatInfo);

    public abstract TickProvider getTickProvider(float tickStep, Unit tickUnit, LabelFormatInfo labelFormatInfo);

}

