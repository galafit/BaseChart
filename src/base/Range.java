package base;

import java.text.MessageFormat;

/**
 * Created by galafit on 11/7/17.
 */
public class Range {
    private double start;
    private double end;

    public Range(double start, double end, boolean isReversed) {
        this.start = start;
        this.end = end;
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

    public Range(double start, double end) {
       this(start, end, false);
    }

    public  double start() {
        return start;
    }

    public double end() {
        return end;
    }

    public double length() {
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