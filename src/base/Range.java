package base;

import java.text.MessageFormat;

/**
 * Created by galafit on 11/7/17.
 */
public class Range {
    private float start;
    private float end;
    private boolean isReversed = false;

    public Range(float start, float end, boolean isReversed) {
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

    public Range(float start, float end) {
       this(start, end, false);
    }

    public boolean contains(float value) {
        if(isReversed && value >= end && value <= start) {
            return true;
        }
        if(!isReversed && value >= start && value <= end) {
            return true;
        }
        return false;
    }

    public  float start() {
        return start;
    }

    public float end() {
        return end;
    }

    public float length() {
        return Math.abs(end - start);
    }

    public static Range max(Range range1, Range range2) {
        return max(range1, range2, false);
    }

    public static Range max(Range range1, Range range2, boolean isReversed) {
        if(range1 == null) {
            return range2;
        }
        if(range2 == null) {
            return range1;
        }
        if(isReversed) {
            return new Range(Math.max(range1.start(), range2.start()), Math.min(range1.end(), range2.end()), true);

        }
        return new Range(Math.min(range1.start(), range2.start()), Math.max(range1.end(), range2.end()));
    }

    @Override
    public String toString() {
        return "Range{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}