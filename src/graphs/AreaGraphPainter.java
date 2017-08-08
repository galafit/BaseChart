package graphs;

import axis.Axis;
import data.Range;
import data.XYOrderedSet;
import data.XYPoint;
import data.XYSet;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/7/17.
 */
public class AreaGraphPainter extends GraphPainter<Number> {
    int pointRadius = 3;

    @Override
    public void draw(XYSet<Number> dataPoints, Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        this.dataPoints = new XYOrderedSet<Number>(dataPoints);
        this.area = area;
        if (dataPoints == null || dataPoints.size() == 0) {
            return;
        }
        Color lineColor = getSettings().getLineColor();
        g.setColor(lineColor);

        GeneralPath path = new GeneralPath();
        double x_0 = xAxis.valueToPoint(dataPoints.getX(0).doubleValue(), area);
        double y_0 = yAxis.valueToPoint(dataPoints.getY(0).doubleValue(), area);

        double x = x_0;
        double y = y_0;
        path.moveTo(x, y);
        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));

        for (int i = 1; i < dataPoints.size(); i++) {
            x = xAxis.valueToPoint(dataPoints.getX(i).doubleValue(), area);
            y = yAxis.valueToPoint(dataPoints.getY(i).doubleValue(), area);
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));

        }
        g.draw(path);

        path.lineTo(x, area.getY() + area.getHeight());
        path.lineTo(x_0, area.getY() + area.getHeight());
        path.lineTo(x_0, y_0);
        Color transparentColor =new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 100 );
        g.setColor(transparentColor);
        g.fill(path);
        drawHover(g, area,xAxis, yAxis);
    }


    public void drawHover(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        if(hoverIndex >= 0) {
            double x = xAxis.valueToPoint(dataPoints.getX(hoverIndex).doubleValue(), area);
            double y = yAxis.valueToPoint(dataPoints.getY(hoverIndex).doubleValue(), area);
            double pointRadius = this.pointRadius + 4;
            g.setColor(Color.CYAN);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));

        }

    }

}
