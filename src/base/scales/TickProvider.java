package base.scales;

/**
 * Created by galafit on 5/9/17.
 */
public interface TickProvider {
    public Tick getUpperTick(float value);
    public Tick getLowerTick(float value);
    public Tick getNextTick();
    public Tick getPreviousTick();
    public void increaseTickStep(int increaseFactor);
}
