package main.chart.axis;

import java.awt.*;

/**
 * Created by galafit on 13/6/17.
 */
public class TicksSettings {
    private final double DEFAULT_LABEL_PADDING_FACTOR = 0.6; // relative to font size
    private final int DEFAULT_TICK_PIXEL_INTERVAL_FACTOR = 3; // relative to font size
    private String tickLabelFontName = Font.SANS_SERIF;
    private int tickLabelsFontSize = 12;
    private Integer tickLabelsPadding = null;
    private boolean isTickLabelsVisible = true;
    private int ticksAmount = 0;

    // see http://api.highcharts.com/highcharts/xAxis.labels.autoRotation
    private int[] tickLabelAutoRotation = {-45, 90}; // at the moment not used

    private int ticksWidth = 1;
    private int tickSize = 2;
    private int tickPixelInterval;
    private double tickInterval; // in axis unit (tickUnit)

    public int getTicksAmount() {
        return ticksAmount;
    }

    public void setTicksAmount(int ticksAmount) {
        this.ticksAmount = ticksAmount;
    }

    public boolean isTickMarkVisible() {
        return (ticksWidth > 0) ? true : false;
    }

    public String getTickLabelFontName() {
        return tickLabelFontName;
    }
    public void setTickLabelFontName(String tickLabelFontName) {
        this.tickLabelFontName = tickLabelFontName;
    }

    public int getTickLabelsFontSize() {
        return tickLabelsFontSize;
    }

    public void setTickLabelsFontSize(int tickLabelsFontSize) {
        this.tickLabelsFontSize = tickLabelsFontSize;
    }

    public int getTickLabelsPadding() {
        return (tickLabelsPadding == null) ? (int) (tickLabelsFontSize * DEFAULT_LABEL_PADDING_FACTOR) : tickLabelsPadding;
    }

    public void setTickLabelsPadding(int tickLabelsPadding) {
        this.tickLabelsPadding = tickLabelsPadding;
    }

    public boolean isTickLabelsVisible() {
        return isTickLabelsVisible;
    }

    public void setTickLabelsVisible(boolean tickLabelsVisible) {
        isTickLabelsVisible = tickLabelsVisible;
    }

    public int[] getTickLabelAutoRotation() {
        return tickLabelAutoRotation;
    }

    public void setTickLabelAutoRotation(int[] tickLabelAutoRotation) {
        this.tickLabelAutoRotation = tickLabelAutoRotation;
    }

    public int getTicksWidth() {
        return ticksWidth;
    }

    public void setTicksWidth(int ticksWidth) {
        this.ticksWidth = ticksWidth;
    }

    public int getTickSize() {
        return tickSize;
    }

    public void setTickSize(int tickSize) {
        this.tickSize = tickSize;
    }

    public int getTickPixelInterval() {
        return (tickPixelInterval != 0) ? tickPixelInterval : getTickLabelsFontSize() * DEFAULT_TICK_PIXEL_INTERVAL_FACTOR;
    }

    public void setTickPixelInterval(int tickPixelInterval) {
        this.tickPixelInterval = tickPixelInterval;
    }

    public double getTickInterval() {
        return tickInterval;
    }

    public void setTickInterval(double tickInterval) {
        this.tickInterval = tickInterval;
    }
}
