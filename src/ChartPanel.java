import base.chart.ChangeListener;
import base.chart.ChartEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by hdablin on 23.06.17.
 */
public class ChartPanel extends JPanel {
    private Chart chart;
    int i;

    public ChartPanel(Chart chart) {
        this.chart = chart;
        this.chart.addChangeListener(new ChangeListener() {
            @Override
            public void update() {
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                chart.mouseMoved(e.getX(), e.getY());

            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                i++;
                if(e.getClickCount() == 2) {
                    chart.mouseDoubleClicked(e.getX(), e.getY());
                    System.out.println("double click: "+i);
                }
                else if(e.getClickCount() == 1) {
                    chart.mouseClicked(e.getX(), e.getY());
                    if (chart.isMouseInsidePreview(e.getX(), e.getY()) && !chart.isMouseInsideScroll(e.getX(), e.getY())) {
                        chart.moveScroll(e.getX(), e.getY());
                        repaint();
                    }
                    System.out.println("single click: "+i);
                }

            }

        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                chart.mouseWheelMoved(e.getX(), e.getY(), e.getWheelRotation());
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chart.draw((Graphics2D) g);
    }
}
