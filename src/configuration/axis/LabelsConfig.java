package configuration.axis;

import configuration.general.TextStyle;

/**
 * Created by galafit on 14/9/17.
 */
public class LabelsConfig {
    public TextStyle textStyle = new TextStyle();
    public int padding = (int)(0.5 * textStyle.fontSize); // px
    public boolean isVisible = true;
    public LabelFormatInfo formatInfo;
    // see http://api.highcharts.com/highcharts/xAxis.labels.autoRotation
   // public int[] autoRotation = {-45, 90}; // at the moment not used
}
