package axis;

import configuration.TickFormatInfo;

/**
 * Created by galafit on 5/9/17.
 */
public interface TickProvider {
    public Tick getTickRight(double value);
    public Tick getTickLeft(double value);
    public Tick getNext();

    public void setTickFormatInfo(TickFormatInfo tickFormatInfo);
    public void setTickAmount(int amount);
    public void setTickStep(double step);
}
