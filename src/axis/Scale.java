package axis;


public abstract class Scale {
    private double domain[] = {0, 1};
    private double range[] = {0, 1};


    public void setDomain(double... domain) {
        this.domain = domain;
    }

    public void setRange(double... range) {
        this.range = range;
    }

    public abstract double scale(double value);

    public abstract double invert(double value);

    public abstract TickProvider getTickProvider();
}

