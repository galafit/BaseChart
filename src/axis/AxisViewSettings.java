package main.chart.axis;

import java.awt.*;

/**
 * Created by hdablin on 13.04.17.
 */
public class AxisViewSettings {
    private final double DEFAULT_NAME_PADDING_FACTOR = 0.8;
    private String nameFontName = Font.SANS_SERIF;
    private Color axisColor = Color.GRAY;
    private int axisLineWidth = 1;
    private boolean isVisible = true;
    private int nameFontSize = 12;
    private Integer namePadding = null;

    private boolean isNameVisible = false;
    private boolean isUnitsVisible = false;

    public String getNameFontName() {
        return nameFontName;
    }

    public void setNameFontName(String nameFontName) {
        this.nameFontName = nameFontName;
    }

    public boolean isUnitsVisible() {
        return isUnitsVisible;
    }

    public void setUnitsVisible(boolean unitsVisible) {
        isUnitsVisible = unitsVisible;
    }

    public boolean isNameVisible() {
        return isNameVisible;
    }

    public void setNameVisible(boolean nameVisible) {
        isNameVisible = nameVisible;
    }

    public int getNameFontSize() {
        return nameFontSize;
    }

    public void setNameFontSize(int nameFontSize) {
        this.nameFontSize = nameFontSize;
    }

    public Integer getNamePadding() {
        return (namePadding == null) ? (int)(DEFAULT_NAME_PADDING_FACTOR * nameFontSize) : namePadding;
    }

    public void setNamePadding(Integer namePadding) {
        this.namePadding = namePadding;
    }

    public boolean isAxisLineVisible() {
        return (axisLineWidth > 0) ? true : false;
    }


    public Color getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
    }

    public int getAxisLineWidth() {
        return axisLineWidth;
    }

    public void setAxisLineWidth(int axisLineWidth) {
        this.axisLineWidth = axisLineWidth;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
