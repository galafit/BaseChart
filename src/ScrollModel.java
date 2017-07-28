/**
 * https://docs.google.com/document/d/1x4MSKJopdGXbtrOhlEc4gD2hA0fKTB2f4ps3F2z4Dgw/edit
 */
public class ScrollModel {
    private double viewportPosition = 0;
    private double min = 0;
    private double max = 1;
    private double viewportWidth = 1;


    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getViewportWidth() {
        return viewportWidth;
    }


    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setViewportWidth(double viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public double getViewportPosition() {
        return viewportPosition;
    }

    public void setViewportPosition(double newViewportPosition) {
        if (newViewportPosition > getMax() - getViewportWidth()) {
            newViewportPosition = getMax() - getViewportWidth();
        }
        if (newViewportPosition < getMin()){
            newViewportPosition = getMin();
        }
        viewportPosition = newViewportPosition;
    }
}
