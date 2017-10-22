package base.chart;

/**
 * Created by galafit on 20/10/17.
 */
public interface BaseMouseListener {
    public void mouseClicked(int mouseX, int mouseY);
    public void mouseDoubleClicked(int mouseX, int mouseY);
    public void mouseMoved(int mouseX, int mouseY);
    public void mouseWheelMoved(int mouseX, int mouseY, int wheelRotation);
}
