package com.biorecorder.basechart.chart.config;


import com.biorecorder.basechart.chart.BColor;

/**
 * Created by galafit on 1/10/17.
 */
public class ScrollConfig {
    private BColor scrollColor = BColor.RED;
    private int scrollMinWidth = 10; //px


    public BColor getScrollColor() {
        return scrollColor;
    }

    public int getScrollMinWidth() {
        return scrollMinWidth;
    }

}
