import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

/**
 * Created by hdablin on 26.06.17.
 */
public class PreviewChartPanel extends JPanel {
    private ChartWithPreview chartWithPreview;
    private boolean isMousePressedInsideCursor = false;
    private int mousePressedX;
    private HoverPanel hoverPanel = new HoverPanel();
    private BufferedImage bgImage;


    public PreviewChartPanel(ChartWithPreview chartWithPreview) {
        this.chartWithPreview = chartWithPreview;
        setBackground(Color.black);

        setLayout(new BorderLayout());
        add(hoverPanel, BorderLayout.CENTER);

        setToolTipText("hello");
        ToolTipManager.sharedInstance().setInitialDelay(0);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (chartWithPreview.isMouseInPreviewArea(e.getX(), e.getY()) && !isMousePressedInsideCursor) {
                    chartWithPreview.setCursorPosition(e.getX());
                    drawToBgImage();
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
                    drawToBgImage();
                    repaint();
                    mousePressedX = e.getX();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if(chartWithPreview.hover(e.getX(), e.getY())) {
                    hoverPanel.repaint();
                }
            }
        });
    }


    public void update() {
        chartWithPreview.update();
        repaint();
    }

    private void drawToBgImage() {
       // bgImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
       // bgImage = (BufferedImage)(this.createImage(getWidth(), getHeight()));

        bgImage = getGraphicsConfiguration().createCompatibleImage(getWidth(), getHeight());
        Graphics2D g = bgImage.createGraphics();
       // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        chartWithPreview.draw(g, new Rectangle(0, 0, getWidth(), getHeight()));
        g.dispose();
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        return chartWithPreview.getTooltipText();
    }

    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return event.getPoint();
    }

    @Override
    protected void paintComponent(Graphics g) {
      /* super.paintComponent(g);
        if(bgImage == null) {
            drawToBgImage();
        }
        g.drawImage(bgImage, 0, 0, this); */
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
