package main.chart.axis;

/**
 * Created by galafit on 5/7/17.
 */
public interface TickProvider {
    public Tick getClosestTickNext(double value);
    public Tick getClosestTickPrev(double value);
    public Tick getNext();
    public void setMinTickPixelInterval(double minTickPixelInterval);
}
