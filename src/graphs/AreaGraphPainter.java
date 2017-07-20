package graphs;

import axis.Axis;
import data.DataPointSet;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/7/17.
 */
public class AreaGraphPainter extends GraphPainter<Double> {
    @Override
    public void draw(DataPointSet<Double> dataPoints, Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        if (dataPoints == null || dataPoints.size() == 0) {
            return;
        }
        Color lineColor = getSettings().getLineColor();
        g.setColor(lineColor);

        GeneralPath path = new GeneralPath();
        double x_0 = xAxis.valueToPoint(dataPoints.getX(0), area);
        double y_0 = yAxis.valueToPoint(dataPoints.getY(0), area);

        double x = x_0;
        double y = y_0;
        path.moveTo(x, y);
        for (int i = 1; i < dataPoints.size(); i++) {
            x = xAxis.valueToPoint(dataPoints.getX(i), area);
            y = yAxis.valueToPoint(dataPoints.getY(i), area);
            path.lineTo(x, y);
        }
        g.draw(path);

        path.lineTo(x, area.getY() + area.getHeight());
        path.lineTo(x_0, area.getY() + area.getHeight());
        path.lineTo(x_0, y_0);
        Color transparentColor =new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 100 );
        g.setColor(transparentColor);
        g.fill(path);
    }
}