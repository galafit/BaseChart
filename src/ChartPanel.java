import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by hdablin on 23.06.17.
 */
public class ChartPanel extends JPanel {
    private Chart chart;

    public ChartPanel(Chart chart) {
        this.chart = chart;
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if(chart.hover(e.getX(), e.getY())) {
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chart.draw((Graphics2D) g);
    }
}
