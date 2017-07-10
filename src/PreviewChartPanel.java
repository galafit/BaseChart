import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by hdablin on 26.06.17.
 */
public class PreviewChartPanel extends JPanel {
    private ChartWithPreview chartWithPreview;
    private boolean isMousePressedInsideCursor = false;
    private int mousePressedX;

    public PreviewChartPanel(ChartWithPreview chartWithPreview) {
        this.chartWithPreview = chartWithPreview;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (chartWithPreview.isMouseInPreviewArea(e.getX(), e.getY()) && !isMousePressedInsideCursor){
                    chartWithPreview.setCursorPosition(e.getX());
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mousePressedX = e.getX();
                isMousePressedInsideCursor = chartWithPreview.isMouseInsideCursor(e.getX(),e.getY());
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
               if (isMousePressedInsideCursor){
                    chartWithPreview.moveCursorPosition(e.getX() - mousePressedX);
                    repaint();
                    mousePressedX = e.getX();
                }
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chartWithPreview.draw((Graphics2D) g, new Rectangle(0,0,getWidth(),getHeight()));
    }

    public void update(){
        chartWithPreview.update();
        repaint();
    }

}
