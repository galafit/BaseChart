package traces;

import axis.Axis;
import configuration.TraceConfig;
import data.XYData;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/9/17.
 */
public class LineTrace extends Trace {
    private XYData data;

    @Override
    public void setConfig(TraceConfig traceConfig) {
        this.traceConfig = traceConfig;
        data = (XYData) traceConfig.getData();
    }


    @Override
    public void draw(Graphics2D g, Axis xAxis, Axis yAxis) {
        if (data == null || data.size() == 0) {
            return;
        }
        g.setColor(traceConfig.getLineColor());
        GeneralPath path = new GeneralPath();
        double x = xAxis.scale(data.getX(0));
        double y = yAxis.scale(data.getY(0));

        path.moveTo(x, y);

        int pointRadius = traceConfig.markConfig.size / 2;
        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        for (int i = 1; i < data.size(); i++) {
            x = xAxis.scale(data.getX(i));
            y = yAxis.scale(data.getY(i));
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
        g.draw(path);
        drawHover(g, xAxis, yAxis);
    }


    public void drawHover(Graphics2D g, Axis xAxis, Axis yAxis) {
        if(hoverIndex >= 0) {
            double x = xAxis.scale(data.getX(hoverIndex));
            double y = yAxis.scale(data.getY(hoverIndex));
            double pointRadius = traceConfig.getHoverSize();
            g.setColor(Color.CYAN);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
    }

}
