package axis;

import configuration.TickFormatInfo;

import java.util.List;

/**
 * Created by galafit on 5/9/17.
 */
public interface TickProvider {
    public void setTickFormatInfo(TickFormatInfo tickFormatInfo);
    public void setTickAmount(int amount);
    public void setTickStep(double step);
    public double getRoundMin();
    public double getRoundMax();

    public List<Tick> getTicks(int ticksDivider);
    public List<Double> getMinorTicks(int ticksDivider, int minorTickCount);
}
