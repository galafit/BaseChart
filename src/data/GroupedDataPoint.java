package data;

/**
 * Created by galafit on 16/7/17.
 */
public class GroupedDataPoint<Y> extends DataPoint<Y> {
    private long groupStartIndex;
    private int groupLength;

    public GroupedDataPoint(double x, Y y, long groupStartIndex, int groupLength) {
        super(x, y);
        this.groupStartIndex = groupStartIndex;
        this.groupLength = groupLength;
    }

    public long getGroupStartIndex() {
        return groupStartIndex;
    }

    public int getGroupLength() {
        return groupLength;
    }
}
