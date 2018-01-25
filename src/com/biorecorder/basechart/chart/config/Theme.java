package com.biorecorder.basechart.chart.config;

import com.biorecorder.basechart.chart.BColor;

/**
 * Chart color themes
 */
public enum Theme {
    WHITE(1),
    DARK(2),
    GRAY(3);

    private BColor chartBgColor;
    private BColor chartMarginColor;

    private BColor previewBgColor;
    private BColor previewMarginColor;
    private BColor titleColor;

    private BColor axisColor;
    private BColor gridColor;
    private BColor[] traceColors;

    private BColor crosshairColor;
    private BColor scrollColor;

    Theme(int themeId) {
        if(themeId == 1) { // WHITE
            final BColor DARK_CYAN = new BColor(60, 170, 180);
            final BColor DARK_BLUE = new BColor(40, 120, 230); //new BColor(37, 100, 250);
            final BColor DARK_MAGENTA = new BColor(150, 80, 150);
            final BColor DARK_GREEN = new BColor(40, 180, 15);
            final BColor DARK_RED = new BColor(200, 10, 70);
            final BColor DARK_ORANGE = new BColor(138, 85, 74);//new BColor(173, 105, 49);
            final BColor DARK_YELLOW = new BColor(255, 127, 15);
            final BColor DARK_PINK = new BColor(100, 110, 140);

            chartBgColor = new BColor(255, 255, 255);
            chartMarginColor = chartBgColor;

            previewBgColor = new BColor(245, 245, 250);
            previewMarginColor = previewBgColor;

            titleColor =new BColor(110, 100, 80);// new BColor(90, 100, 120);
            axisColor = titleColor;
            gridColor = new BColor(215, 208, 200);

            BColor[] colors = {DARK_BLUE, DARK_MAGENTA, DARK_PINK, DARK_RED, DARK_ORANGE, DARK_YELLOW, DARK_GREEN, DARK_CYAN};
            traceColors = colors;

            crosshairColor = BColor.RED;
            scrollColor = BColor.RED;

        }
        if(themeId == 2) { // DARK
            final BColor CYAN = new BColor(0, 200, 230);
            final BColor BLUE = new BColor(100, 120, 250);
            final BColor MAGENTA = new BColor(165, 80, 190); //new BColor(200, 40, 250);
            final BColor GREEN = new BColor(120, 250, 123);//new BColor(77, 184, 118);//new BColor(0, 204, 31);
            final BColor RED = new BColor(250, 64, 82);//new BColor(191, 60, 54);
            final BColor ORANGE = new BColor(200, 80, 0);//new BColor(173, 105, 49);
            final BColor YELLOW = new BColor(252, 177, 48);
            final BColor PINK = BColor.PINK;
            final BColor BROWN = new BColor(130, 110, 80);//new BColor(120, 94, 50); //new BColor(145, 94, 32);//new BColor(163, 106, 36); //new BColor(125, 81, 26);
            final BColor DARK_BROWN = new BColor(60, 55, 35); // new BColor(64, 56, 40);

            chartBgColor = BColor.BLACK;
            chartMarginColor = chartBgColor;
            titleColor = BROWN;

            previewBgColor = new BColor(12, 18, 28);
            previewMarginColor = previewBgColor;

            axisColor = BROWN;
            gridColor = DARK_BROWN;

            BColor[] colors = {BLUE, RED, PINK, MAGENTA, ORANGE, YELLOW, GREEN, CYAN};
            traceColors = colors;

            crosshairColor = BColor.RED;
            scrollColor = BColor.RED;
        }
        if(themeId == 3) { // GRAY
            final BColor BLUE = new BColor(0, 130, 230);
            final BColor BLUE_DARK = new BColor(30, 30, 180);
            final BColor SEA = new BColor(30, 130, 120);

            chartBgColor = new BColor(170, 175, 185);
            chartMarginColor = chartBgColor;

            previewBgColor = new BColor(170, 160, 170);
            previewMarginColor = previewBgColor;

            titleColor = BColor.BLACK;
            axisColor = new BColor(70, 60, 40);
            gridColor = new BColor(120, 110, 100);

            BColor[] colors = {BLUE_DARK, BColor.RED, BColor.BLACK, BColor.MAGENTA, BLUE, BColor.GREEN, BColor.YELLOW, SEA};
            traceColors = colors;

            crosshairColor = BColor.RED;
            scrollColor = BColor.RED;

        }
    }

    public BColor getCrosshairColor() {
        return crosshairColor;
    }

    public BColor getScrollColor() {
        return scrollColor;
    }

    public BColor getChartBgColor() {
        return chartBgColor;
    }

    public BColor getChartMarginColor() {
        return chartMarginColor;
    }

    public BColor getPreviewBgColor() {
        return previewBgColor;
    }

    public BColor getPreviewMarginColor() {
        return previewMarginColor;
    }

    public BColor getTitleColor() {
        return titleColor;
    }

    public BColor getAxisColor() {
        return axisColor;
    }

    public BColor getGridColor() {
        return gridColor;
    }

    public BColor[] getTraceColors() {
        return traceColors;
    }
}
