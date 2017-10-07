package base;

import base.Range;

/**
 * Created by galafit on 6/10/17.
 */
public interface XYData {
    public int size();

    public double getX(int index);

    public double getY(int index);

    public Range getYExtremes();

    public Range getXExtremes();

    public int findNearest(double xValue);
}
