import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (chart.isMouseInsidePreview(e.getX(), e.getY()) && !chart.isMouseInsideScroll(e.getX(), e.getY())) {
                    chart.moveScroll(e.getX(), e.getY());
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
