package configuration;

/**
 * Created by galafit on 6/9/17.
 */
public class TicksConfig {
    public int tickMarkWidth = 1; // px
    public int tickMarkInsideSize = 0; // px
    public int tickMarkOutsideSize = 3; // px
    public TickFormatInfo formatInfo;
    public TextStyle textStyle = new TextStyle();

    public int padding = (int)(0.5 * textStyle.fontSize); // px

    // see http://api.highcharts.com/highcharts/xAxis.labels.autoRotation
    public int[] autoRotation = {-45, 90}; // at the moment not used
    public boolean isLabelsVisible = true;

    public int tickPixelInterval = 0;
    public double tickStep; // in data unit
    public int ticksAmount = 0;


    public boolean isTickMarksVisible() {
        return (tickMarkWidth > 0) ? true : false;
    }
}
