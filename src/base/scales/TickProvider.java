package base.scales;

import base.config.axis.LabelFormatInfo;

import java.util.List;

/**
 * Created by galafit on 5/9/17.
 */
public interface TickProvider {
    public void setLabelFormatInfo(LabelFormatInfo labelFormatInfo);
    public void setTickAmount(int amount);
    public void setTickStep(double step);
    public double getRoundMin();
    public double getRoundMax();

    public List<Tick> getTicks(int ticksDivider);
    public List<Double> getMinorTicks(int ticksDivider, int minorTickCount);
}
