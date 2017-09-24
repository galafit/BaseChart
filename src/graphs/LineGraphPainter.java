package graphs;

import axis.Axis;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/7/17.
 */
public class LineGraphPainter extends GraphPainter<Number> {
    int pointRadius = 5;

    @Override
    public void draw(Graphics2D g, Axis xAxis, Axis yAxis) {
        if (dataPoints == null || dataPoints.size() == 0) {
            return;
        }
        g.setColor(getSettings().getLineColor());
        GeneralPath path = new GeneralPath();
        double x = xAxis.scale(dataPoints.getX(0).doubleValue());
        double y = yAxis.scale(dataPoints.getY(0).doubleValue());

        path.moveTo(x, y);

        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        for (int i = 1; i < dataPoints.size(); i++) {
            x = xAxis.scale(dataPoints.getX(i).doubleValue());
            y = yAxis.scale(dataPoints.getY(i).doubleValue());
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
        g.draw(path);
    }

    @Override
    public void drawHover(Graphics2D g, Axis xAxis, Axis yAxis) {
        if(hoverIndex >= 0) {
            double x = xAxis.scale(dataPoints.getX(hoverIndex).doubleValue());
            double y = yAxis.scale(dataPoints.getY(hoverIndex).doubleValue());
            double pointRadius = this.pointRadius + 4;
            g.setColor(Color.CYAN);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
     }
}
