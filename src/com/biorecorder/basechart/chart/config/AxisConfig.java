package com.biorecorder.basechart.chart.config;

import com.biorecorder.basechart.chart.BColor;
import com.biorecorder.basechart.chart.BStroke;
import com.biorecorder.basechart.chart.TextStyle;

/**
 * Created by galafit on 5/9/17.
 */
public class AxisConfig {
    private AxisOrientation orientation;
    private AxisType axisType = AxisType.LINEAR;
    private BColor color =  BColor.BROWN; // BColor.GRAY;
    private BColor gridColor = BColor.BROWN_LIGHT; //BColor(100, 100, 100);
    private BColor minorGridColor = BColor.BROWN_LIGHT;//new BColor(80, 80, 80);

    private TextStyle labelTextStyle = new TextStyle(TextStyle.DEFAULT, TextStyle.NORMAL, 12);
    private int labelPadding; // px
    private LabelFormatInfo labelFormatInfo = new LabelFormatInfo();
    private boolean isLabelsVisible = true;
    private boolean isLabelInside = false;

    private int tickMarkWidth = 1; // px
    private int tickMarkInsideSize = 0; // px
    private int tickMarkOutsideSize = 3; // px
    private boolean isTicksVisible = true;
    private float tickStep = 0; // in domain units

    private String title;
    private TextStyle titleTextStyle = new TextStyle(TextStyle.DEFAULT, TextStyle.NORMAL, 14);
    private int titlePadding; // px

    private boolean isVisible = false;
    private BStroke axisLineStroke = new BStroke(1);
    private BStroke gridLineStroke = new BStroke(0);
    private BStroke minorGridLineStroke = new BStroke(0);
    private int minorGridCounter = 3; // minor grid divider

    private boolean isMinMaxRoundingEnable = false;

    public AxisConfig(AxisOrientation orientation) {
        this.orientation = orientation;
        titlePadding = (int)(0.4 * titleTextStyle.getSize());
        labelPadding = (int)(0.3 * labelTextStyle.getSize());
    }

    public AxisConfig(AxisConfig axisConfig) {
        orientation = axisConfig.orientation;
        color = axisConfig.color;
        gridColor = axisConfig.gridColor;
        minorGridColor = axisConfig.minorGridColor;

        labelTextStyle = new TextStyle(axisConfig.labelTextStyle);
        labelPadding = axisConfig.labelPadding;
        labelFormatInfo = new LabelFormatInfo(axisConfig.labelFormatInfo);
        isLabelsVisible = axisConfig.isLabelsVisible;
        isLabelInside = axisConfig.isLabelInside;

        tickMarkWidth = axisConfig.tickMarkWidth;
        tickMarkInsideSize = axisConfig.tickMarkInsideSize;
        tickMarkOutsideSize = axisConfig.tickMarkOutsideSize;
        isTicksVisible = axisConfig.isTicksVisible();
        tickStep = axisConfig.tickStep;

        title = axisConfig.title;
        titleTextStyle = new TextStyle(axisConfig.titleTextStyle);
        titlePadding = axisConfig.titlePadding;

        isVisible = axisConfig.isVisible;
        axisLineStroke = new BStroke(axisConfig.axisLineStroke);
        gridLineStroke = new BStroke(axisConfig.gridLineStroke);
        minorGridLineStroke = new BStroke(axisConfig.minorGridLineStroke);
        minorGridCounter = axisConfig.minorGridCounter;

        isMinMaxRoundingEnable = axisConfig.isMinMaxRoundingEnable;
    }

    public AxisType getAxisType() {
        return axisType;
    }

    public void setAxisType(AxisType axisType) {
        this.axisType = axisType;
    }

    public boolean isLabelInside() {
        return isLabelInside;
    }

    public void setLabelInside(boolean labelInside) {
        isLabelInside = labelInside;
    }

    public boolean isMinMaxRoundingEnable() {
        return isMinMaxRoundingEnable;
    }

    public void setMinMaxRoundingEnable(boolean isMinMaxRoundingEvalable) {
        this.isMinMaxRoundingEnable = isMinMaxRoundingEvalable;
    }

    public BColor getColor() {
        return color;
    }

    public void setColor(BColor color) {
        this.color = color;
    }

    public void setGridColor(BColor gridColor) {
        this.gridColor = gridColor;
    }

    public void setMinorGridColor(BColor minorGridColor) {
        this.minorGridColor = minorGridColor;
    }

    public BColor getGridColor() {
        return (gridColor != null) ? gridColor : color;
    }

    public BColor getMinorGridColor() {
        return (minorGridColor != null) ? minorGridColor : color;
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

    public float getTickStep() {
        return tickStep;
    }

    public void setTickStep(float tickStep) {
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

    public BStroke getAxisLineStroke() {
        return axisLineStroke;
    }

    public BStroke getGridLineStroke() {
        return gridLineStroke;
    }

    public BStroke getMinorGridLineStroke() {
        return minorGridLineStroke;
    }

    public void setAxisLineStroke(BStroke axisLineStyle) {
        this.axisLineStroke = axisLineStyle;
    }

    public void setGridLineStroke(BStroke gridLineStyle) {
        this.gridLineStroke = gridLineStyle;
    }

    public void setMinorGridLineStroke(BStroke minorGridLineStyle) {
        this.minorGridLineStroke = minorGridLineStyle;
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

    public BColor getLabelsColor() {
        return color;
    }

    public BColor getTitleColor() {
        return color;
    }

    public BColor getTicksColor() {
        return color;
    }

    public boolean isAxisLineVisible() {
        if(axisLineStroke.getWidth() > 0) {
            return true;
        }
        return false;
    }

    public boolean isGridVisible() {
        if(gridLineStroke.getWidth() > 0) {
            return true;
        }
        return false;
    }

    public boolean isMinorGridVisible() {
        if(minorGridLineStroke.getWidth() > 0) {
            return true;
        }
        return false;
    }
}