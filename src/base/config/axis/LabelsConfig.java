package base.config.axis;

import base.config.general.TextStyle;

/**
 * Created by galafit on 14/9/17.
 */
public class LabelsConfig {
    private TextStyle textStyle = new TextStyle();
    private int padding = (int)(0.5 * getTextStyle().getFontSize()); // px
    private boolean isVisible = true;
    private LabelFormatInfo formatInfo = new LabelFormatInfo();

    public TextStyle getTextStyle() {
        return textStyle;
    }


    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public LabelFormatInfo getFormatInfo() {
        return formatInfo;
    }

    // see http://api.highcharts.com/highcharts/xAxis.labels.autoRotation
   // public int[] autoRotation = {-45, 90}; // at the moment not used
}
