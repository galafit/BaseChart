import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by hdablin on 26.06.17.
 */
public class PreviewChartPanel extends JPanel {
    private ChartWithPreview chartWithPreview;
    private boolean isMousePressedInsideCursor = false;
    private int mousePressedX;
    private BufferedImage bgImage;


    public PreviewChartPanel(ChartWithPreview chartWithPreview) {
        this.chartWithPreview = chartWithPreview;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (chartWithPreview.isMouseInPreviewArea(e.getX(), e.getY()) && !isMousePressedInsideCursor) {
                    chartWithPreview.setCursorPosition(e.getX());
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mousePressedX = e.getX();
                isMousePressedInsideCursor = chartWithPreview.isMouseInsideCursor(e.getX(), e.getY());
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (isMousePressedInsideCursor) {
                    chartWithPreview.moveCursorPosition(e.getX() - mousePressedX);
                    repaint();
                    mousePressedX = e.getX();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if(chartWithPreview.hover(e.getX(), e.getY())) {
                    repaint();
                }
            }
        });
    }


    public void update() {
        chartWithPreview.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
       super.paintComponent(g);
        chartWithPreview.draw((Graphics2D) g, new Rectangle(0, 0, getWidth(), getHeight()));
    }
}
