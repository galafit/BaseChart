package base.chart;

/**
 * Created by galafit on 20/10/17.
 */
public interface ChartEventListener {
    public void hoverChanged();

    /**
     * Trigger when some action on the xAxis is performed
     * @param xAxisIndex
     * @param actionDirection additional parameter that can be > 0 or < 0
     */
    public void xAxisActionPerformed(int xAxisIndex, int actionDirection);
    public void yAxisActionPerformed(int yAxisIndex, int actionDirection);
    public void yAxisResetActionPerformed(int yAxisIndex);
}
