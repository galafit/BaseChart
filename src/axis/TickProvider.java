package axis;

/**
 * Created by galafit on 5/9/17.
 */
public interface TickProvider {
    public Tick getClosestTickRight(double value);
    public Tick getClosestTickLeft(double value);
    public Tick getNext();
}
