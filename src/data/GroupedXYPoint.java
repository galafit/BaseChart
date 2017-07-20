package data;

/**
 * Created by galafit on 16/7/17.
 */
public class GroupedXYPoint<Y> extends XYPoint<Y> {
    private long groupStartIndex;
    private int groupLength;

    public GroupedXYPoint(double x, Y y, long groupStartIndex, int groupLength) {
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
