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
    private HoverPanel hoverPanel = new HoverPanel();


    public PreviewChartPanel(ChartWithPreview chartWithPreview) {
        setLayout(new BorderLayout());
        this.chartWithPreview = chartWithPreview;
        setBackground(Color.black);
        add(hoverPanel, BorderLayout.CENTER);

        // setToolTipText("hello");
        // ToolTipManager.sharedInstance().setInitialDelay(0);

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
                chartWithPreview.hover(e.getX(), e.getY());
                hoverPanel.repaint();
            }
        });
    }


    public void update() {
        chartWithPreview.update();
        repaint();
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        Point p = new Point(event.getX(), event.getY());

        return "point: " + p.x;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chartWithPreview.draw((Graphics2D) g, new Rectangle(0, 0, getWidth(), getHeight()));
    }

    class HoverPanel extends JPanel {
        public HoverPanel() {
            setOpaque(false);
            //setSize(300,300);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            chartWithPreview.drawHover((Graphics2D) g, new Rectangle(0, 0, getWidth(), getHeight()));
        }
    }

}
