/**
 * https://docs.google.com/document/d/1x4MSKJopdGXbtrOhlEc4gD2hA0fKTB2f4ps3F2z4Dgw/edit
 */
public interface ScrollModel {
    public long getMin();
    public long getMax();
    public long getViewportWidth();
    public long getViewportPosition();
    public void setViewportPosition(long newPosition);
}
