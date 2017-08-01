package graphs;

import axis.Axis;
import data.XYOrderedSet;
import data.XYPoint;
import data.XYSet;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/7/17.
 */
public class LineGraphPainter extends GraphPainter<Double> {
    int pointRadius = 5;
    private XYOrderedSet<Double> dataPoints;
    int hoverIndex = -1;
    Rectangle area;
    int hoverRadius = 10;

    public XYPoint<Double> getHoverPoint(){
        if (hoverIndex >= 0) {
            return new XYPoint<Double>(dataPoints.getX(hoverIndex), dataPoints.getY(hoverIndex));
        }
        return null;
    }

    public boolean hover(int mouseX, int mouseY, Axis xAxis, Axis yAxis) {
        int hoverIndex;

        if(area.contains(new Point(mouseX, mouseY))) {
            double xValue = xAxis.pointsToValue(mouseX, area);
            hoverIndex = dataPoints.getNearestPoint(xValue);
            if (hoverIndex >= 0){
                double y = dataPoints.getY(hoverIndex);
                int yPoint = (int)yAxis.valueToPoint(y,area);
                if (Math.abs(yPoint - mouseY) > hoverRadius){
                    hoverIndex = - 1;
                }
            }
        }
        else {
            hoverIndex = - 1;
        }


        if(this.hoverIndex != hoverIndex) {
            this.hoverIndex = hoverIndex;
            return true;
        }
        return false;
    }

    @Override
    public void draw(XYSet<Double> dataPoints , Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        this.dataPoints = new XYOrderedSet<Double>(dataPoints);
        this.area = area;
        if (dataPoints == null || dataPoints.size() == 0) {
            return;
        }
        g.setColor(getSettings().getLineColor());
        GeneralPath path = new GeneralPath();
        double x = xAxis.valueToPoint(dataPoints.getX(0), area);
        double y = yAxis.valueToPoint(dataPoints.getY(0), area);

        path.moveTo(x, y);

        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        for (int i = 1; i <dataPoints.size(); i++) {
            x = xAxis.valueToPoint(dataPoints.getX(i), area);
            y = yAxis.valueToPoint(dataPoints.getY(i), area);
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
        g.draw(path);
        drawHover(g, area,xAxis, yAxis);
    }

    @Override
    public void drawHover(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        if(hoverIndex >= 0) {
            double x = xAxis.valueToPoint(dataPoints.getX(hoverIndex), area);
            double y = yAxis.valueToPoint(dataPoints.getY(hoverIndex), area);
            double pointRadius = this.pointRadius + 4;
            g.setColor(Color.CYAN);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));

        }
     }
}
