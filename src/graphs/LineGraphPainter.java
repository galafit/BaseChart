package graphs;

import axis.Axis;
import data.DataPointSet;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/7/17.
 */
public class LineGraphPainter extends GraphPainter<Double> {
    @Override
    public void draw(DataPointSet<Double> dataPoints , Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        if (dataPoints == null || dataPoints.size() == 0) {
            return;
        }
        g.setColor(getSettings().getLineColor());
        GeneralPath path = new GeneralPath();
        double x = xAxis.valueToPoint(dataPoints.getX(0), area);
        double y = yAxis.valueToPoint(dataPoints.getY(0), area);

        path.moveTo(x, y);
        for (int i = 1; i <dataPoints.size(); i++) {
            x = xAxis.valueToPoint(dataPoints.getX(i), area);
            y = yAxis.valueToPoint(dataPoints.getY(i), area);
            path.lineTo(x, y);
        }
        g.draw(path);
    }
}
