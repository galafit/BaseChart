package com.biorecorder.basechart.chart.traces;

import java.text.MessageFormat;

/**
 * Created by galafit on 8/1/18.
 */
public class RangeInt {
    private int start;
    private int end;
    private boolean isReversed = false;

    public RangeInt(int start, int end, boolean isReversed) {
        this.start = start;
        this.end = end;
        this.isReversed = isReversed;
        if(isReversed) { // when start > end
            if (end > start){
                String errorMessage = "Error during creating Reversed Range. Expected Start >= End. Start = {0}, End = {1}.";
                String formattedError = MessageFormat.format(errorMessage,start,end);
                throw new IllegalArgumentException(formattedError);
            }
        } else {
            if (start > end){
                String errorMessage = "Error during creating Range. Expected Start <= End. Start = {0}, End = {1}.";
                String formattedError = MessageFormat.format(errorMessage,start,end);
                throw new IllegalArgumentException(formattedError);
            }
        }
    }

    public RangeInt(int start, int end) {
        this(start, end, false);
    }

    public boolean contains(int value) {
        if(isReversed && value >= end && value <= start) {
            return true;
        }
        if(!isReversed && value >= start && value <= end) {
            return true;
        }
        return false;
    }

    public  int start() {
        return start;
    }

    public int end() {
        return end;
    }

    public int length() {
        return Math.abs(end - start);
    }

    public static RangeInt max(RangeInt range1, RangeInt range2) {
        return max(range1, range2, false);
    }

    public static RangeInt max(RangeInt range1, RangeInt range2, boolean isReversed) {
        if(range1 == null) {
            return range2;
        }
        if(range2 == null) {
            return range1;
        }
        if(isReversed) {
            return new RangeInt(Math.max(range1.start(), range2.start()), Math.min(range1.end(), range2.end()), true);

        }
        return new RangeInt(Math.min(range1.start(), range2.start()), Math.max(range1.end(), range2.end()));
    }
}
