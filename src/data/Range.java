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

    public  double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

}