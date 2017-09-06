package configuration;

/**
 * Created by galafit on 6/9/17.
 */
public class TicksConfig {
    public int tickMarkWidth = 1; // px
    public int tickMarkInsideSize = 0; // px
    public int tickMarkOutsideSize = 2; // px
    public TickLabelFormat labelFormat;
    public TextStyle labelStyle = new TextStyle();

    public int padding = (int)(0.5 * labelStyle.fontSize); // px

    // see http://api.highcharts.com/highcharts/xAxis.labels.autoRotation
    public int[] labelAutoRotation = {-45, 90}; // at the moment not used
    public boolean isLabelsVisible = true;

    public int tickPixelInterval = 0;
    public double tickInterval; // in data unit
    public int ticksAmount = 0;


    public boolean isTickMarksVisible() {
        return (tickMarkWidth > 0) ? true : false;
    }
}
