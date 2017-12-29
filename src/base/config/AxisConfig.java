package base.config;

import base.config.general.LineConfig;
import base.config.general.TextStyle;

import java.awt.*;

/**
 * Created by galafit on 5/9/17.
 */
public class AxisConfig {
    private AxisOrientation orientation;
    private Color color =  Color.GRAY;

    private TextStyle labelTextStyle = new TextStyle();
    private int labelPadding; // px
    private LabelFormatInfo labelFormatInfo = new LabelFormatInfo();
    private boolean isLabelsVisible = true;

    private int tickMarkWidth = 1; // px
    private int tickMarkInsideSize = 0; // px
    private int tickMarkOutsideSize = 3; // px
    private boolean isTicksVisible = true;
    private double tickStep = 0; // in domain units

    private String title;
    private TextStyle titleTextStyle = new TextStyle();
    private int titlePadding; // px

    private boolean isVisible = false;
    private LineConfig axisLineConfig = new LineConfig();
    private LineConfig gridLineConfig = new LineConfig();
    private LineConfig minorGridLineConfig = new LineConfig();
    private int minorGridCounter = 3; // minor grid divider



    private boolean isMinMaxRoundingEnable = false;

    public AxisConfig(AxisOrientation orientation) {
        this.orientation = orientation;
        getGridLineConfig().setWidth(1);
        getGridLineConfig().setColor(new Color(100, 100, 100));
        getMinorGridLineConfig().setWidth(1);
        getMinorGridLineConfig().setColor(new Color(80, 80, 80));
        titleTextStyle.setFontSize(14);
        titlePadding = (int)(0.4 * titleTextStyle.getFontSize());
        labelTextStyle.setFontSize(12);
        labelPadding = (int)(0.5 * labelTextStyle.getFontSize());
    }

    public boolean isMinMaxRoundingEnable() {
        return isMinMaxRoundingEnable;
    }

    public void setMinMaxRoundingEnable(boolean isMinMaxRoundingEvalable) {
        this.isMinMaxRoundingEnable = isMinMaxRoundingEvalable;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }


    /** ======================= Labels ========================== **/

    public TextStyle getLabelTextStyle() {
        return labelTextStyle;
    }

    public void setLabelTextStyle(TextStyle labelTextStyle) {
        this.labelTextStyle = labelTextStyle;
    }

    public int getLabelPadding() {
        return labelPadding;
    }

    public void setLabelPadding(int labelPadding) {
        this.labelPadding = labelPadding;
    }

    public boolean isLabelsVisible() {
        return isLabelsVisible;
    }

    public void setLabelsVisible(boolean isLabelVisible) {
        this.isLabelsVisible = isLabelVisible;
    }

    public LabelFormatInfo getLabelFormatInfo() {
        return labelFormatInfo;
    }

    public void setLabelFormatInfo(LabelFormatInfo labelFormatInfo) {
        this.labelFormatInfo = labelFormatInfo;
    }

    /** ======================= Ticks ========================== **/

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

    public boolean isTicksVisible() {
        return isTicksVisible;
    }

    public void setTicksVisible(boolean ticksVisible) {
        isTicksVisible = ticksVisible;
    }

    public double getTickStep() {
        return tickStep;
    }

    public void setTickStep(double tickStep) {
        this.tickStep = tickStep;
    }

    /** ======================= Title ========================== **/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TextStyle getTitleTextStyle() {
        return titleTextStyle;
    }

    public void setTitleTextStyle(TextStyle titleTextStyle) {
        this.titleTextStyle = titleTextStyle;
    }

    public int getTitlePadding() {
        return titlePadding;
    }

    public void setTitlePadding(int titlePadding) {
        this.titlePadding = titlePadding;
    }

    /** ======================= Axis and Grid lines ========================== **/

    public LineConfig getAxisLineConfig() {
        return axisLineConfig;
    }

    public LineConfig getGridLineConfig() {
        return gridLineConfig;
    }

    public LineConfig getMinorGridLineConfig() {
        return minorGridLineConfig;
    }

    public void setAxisLineConfig(LineConfig axisLineConfig) {
        this.axisLineConfig = axisLineConfig;
    }

    public void setGridLineConfig(LineConfig gridLineConfig) {
        this.gridLineConfig = gridLineConfig;
    }

    public void setMinorGridLineConfig(LineConfig minorGridLineConfig) {
        this.minorGridLineConfig = minorGridLineConfig;
    }

    public void setMinorGridCounter(int minorGridCounter) {
        this.minorGridCounter = minorGridCounter;
    }

    public int getMinorGridCounter() {
        return minorGridCounter;
    }

    /** ======================= Helper Methods ========================== **/

    public boolean isTop() {
        if(orientation == AxisOrientation.TOP) {
            return true;
        }
        return false;
    }

    public boolean isBottom() {
        if(orientation == AxisOrientation.BOTTOM) {
            return true;
        }
        return false;
    }

    public boolean isLeft() {
        if(orientation == AxisOrientation.LEFT) {
            return true;
        }
        return false;
    }

    public boolean isRight() {
        if(orientation == AxisOrientation.RIGHT) {
            return true;
        }
        return false;
    }

    public Color getAxisLineColor() {
        return (getAxisLineConfig().getColor() != null) ? getAxisLineConfig().getColor() : color;
    }

    public Color getGridColor() {
        return (getGridLineConfig().getColor() != null) ? getGridLineConfig().getColor() : color;
    }

    public Color getMinorGridColor() {
        return (getMinorGridLineConfig().getColor() != null) ? getMinorGridLineConfig().getColor() : color;
    }

    public Color getLabelsColor() {
        return (labelTextStyle.getFontColor() != null) ? labelTextStyle.getFontColor() : color;
    }

    public Color getTitleColor() {
        return (titleTextStyle.getFontColor() != null) ? titleTextStyle.getFontColor() : color;
    }

    public Color getTicksColor() {
        return color;
    }
}
