package base.chart;

/**
 * Created by galafit on 20/10/17.
 */
public interface ChartEventListener {
    public void onHoverChanged();

    /**
     * if click is made on "start half" of the axis area clickLocation < 0,
     * on "end half" of the axis area clickLocation > 0
     * @param xAxisIndex
     * @param clickLocation describe where the click was made: close to the axis start (clickLocation < 0) or
     *                      close to the end (clickLocation > 0)
     */
    public void onXAxisClicked(int xAxisIndex, int clickLocation);
    public void onYAxisMouseWheelMoved(int yAxisIndex, int wheelRotation);
    public void onYAxisDoubleClicked(int yAxisIndex);
}
