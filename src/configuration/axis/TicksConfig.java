package configuration.axis;

import configuration.general.TextStyle;

/**
 * Created by galafit on 6/9/17.
 */
public class TicksConfig {
    public int tickMarkWidth = 1; // px
    public int tickMarkInsideSize = 0; // px
    public int tickMarkOutsideSize = 3; // px

    public int tickPixelInterval = 0;
    public double tickStep; // in data_old unit
    public int ticksAmount = 0;


    public boolean isTickMarksVisible() {
        return (tickMarkWidth > 0) ? true : false;
    }
}
