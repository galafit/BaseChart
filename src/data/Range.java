package data;

import java.text.MessageFormat;

/**
 * Created by galafit on 11/7/17.
 */
public class Range {
    public double start;
    public double end;

    public Range(double start, double end) {
        if (start > end){
            String errorMessage = "Error during creating Range. Expected Start <= End. Start = {0}, End = {1}.";
            String formattedError = MessageFormat.format(errorMessage,start,end);
            throw new IllegalArgumentException(formattedError);
        }
        this.start = start;
        this.end = end;
    }

    public  double start() {
        return start;
    }

    public double end() {
        return end;
    }

    public double length() {
        return end - start;
    }

    public static Range max(Range range1, Range range2) {
        if(range1 == null) {
            return range2;
        }
        if(range2 == null) {
            return range1;
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