import axis.Axis;
import data.ExtremesFunction;
import data.Range;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph<Double> {
    public AreaGraph() {
        dataProcessor.setExtremesFunction(new ExtremesFunction<Double>() {
            @Override
            public Range getExtremes(Double value) {
                return new Range(value, value);
            }
        });
    }

    @Override
    public void draw(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        g.setColor(lineColor);

        GeneralPath path = new GeneralPath();
        double x_0 = xAxis.valueToPoint(dataProcessor.getX(0), area);
        double y_0 = yAxis.valueToPoint(dataProcessor.getY(0), area);

        double x = x_0;
        double y = y_0;
        path.moveTo(x, y);
        for (int i = 1; i < dataProcessor.size(); i++) {
            x = xAxis.valueToPoint(dataProcessor.getX(i), area);
            y = yAxis.valueToPoint(dataProcessor.getY(i), area);
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
