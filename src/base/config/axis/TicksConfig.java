package base.config.axis;

/**
 * Created by galafit on 6/9/17.
 */
public class TicksConfig {
    private int tickMarkWidth = 1; // px
    private int tickMarkInsideSize = 0; // px
    private int tickMarkOutsideSize = 3; // px

    private int tickPixelInterval = 0;
    private double tickStep; // in data_old unit
    private int ticksAmount = 0;


    public boolean isTickMarksVisible() {
        return (getTickMarkWidth() > 0) ? true : false;
    }

    public int getTickMarkWidth() {
        return tickMarkWidth;
    }

    public void setTickMarkWidth(int tickMarkWidth) {
        this.tickMarkWidth = tickMarkWidth;
    }

    public int getTickMarkInsideSize() {
        return tickMarkInsideSize;
    }

    public void setTickMarkInsideSize(int tickMarkInsideSize) {
        this.tickMarkInsideSize = tickMarkInsideSize;
    }

    public int getTickMarkOutsideSize() {
        return tickMarkOutsideSize;
    }

    public void setTickMarkOutsideSize(int tickMarkOutsideSize) {
        this.tickMarkOutsideSize = tickMarkOutsideSize;
    }

    public int getTickPixelInterval() {
        return tickPixelInterval;
    }

    public void setTickPixelInterval(int tickPixelInterval) {
        this.tickPixelInterval = tickPixelInterval;
    }

    public double getTickStep() {
        return tickStep;
    }

    public void setTickStep(double tickStep) {
        this.tickStep = tickStep;
    }

    public int getTicksAmount() {
        return ticksAmount;
    }

    public void setTicksAmount(int ticksAmount) {
        this.ticksAmount = ticksAmount;
    }
}
