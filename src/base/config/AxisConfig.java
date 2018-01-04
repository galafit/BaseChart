package base.config;

import base.BColor;
import base.BStroke;
import base.TextStyle;

/**
 * Created by galafit on 5/9/17.
 */
public class AxisConfig {
    private AxisOrientation orientation;
    private BColor color =  BColor.GRAY;
    private BColor gridColor = new BColor(100, 100, 100);
    private BColor minorGridColor = new BColor(80, 80, 80);

    private TextStyle labelTextStyle = new TextStyle(TextStyle.DEFAULT, TextStyle.NORMAL, 12);
    private int labelPadding; // px
    private LabelFormatInfo labelFormatInfo = new LabelFormatInfo();
    private boolean isLabelsVisible = true;

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
    private BStroke gridLineStroke = new BStroke(1);
    private BStroke minorGridLineStroke = new BStroke(0);
    private int minorGridCounter = 3; // minor grid divider

    private boolean isMinMaxRoundingEnable = false;

    public AxisConfig(AxisOrientation orientation) {
        this.orientation = orientation;
        titlePadding = (int)(0.4 * titleTextStyle.getSize());
        labelPadding = (int)(0.5 * labelTextStyle.getSize());
        gridColor = new BColor(100, 100, 100);
        minorGridColor = new BColor(80, 80, 80);
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
