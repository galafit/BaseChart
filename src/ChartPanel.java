import javax.swing.*;
import java.awt.*;

/**
 * Created by hdablin on 23.06.17.
 */
public class ChartPanel extends JPanel {
    private Drawable chart;

    public ChartPanel(Drawable chart) {
        this.chart = chart;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chart.draw((Graphics2D) g, new Rectangle(0,0,getWidth(),getHeight()));
    }

    public void update(){
        chart.update();
        repaint();
    }
}
