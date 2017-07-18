import axis.Axis;
import data.DataProcessor;
import data.ExtremesFunction;
import data.Range;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph<Double> {

    public LineGraph() {
        dataProcessor.setExtremesFunction(new ExtremesFunction<Double>() {
            @Override
            public Range getExtremes(Double value) {
                return new Range(value, value);
            }
        });
    }

    public void draw(Graphics2D g, Rectangle area, Axis xAxis, Axis yAxis) {
        if (dataProcessor == null || dataProcessor.getFullDataSize() == 0 || dataProcessor.size() == 0) {return;}
        g.setColor(lineColor);
        GeneralPath path = new GeneralPath();
        double x = xAxis.valueToPoint(dataProcessor.getX(0), area);
        double y = yAxis.valueToPoint(dataProcessor.getY(0), area);

        path.moveTo(x, y);
        for (int i = 1; i <dataProcessor.size(); i++) {
            x = xAxis.valueToPoint(dataProcessor.getX(i), area);
            y = yAxis.valueToPoint(dataProcessor.getY(i), area);
            path.lineTo(x, y);
        }
        g.draw(path);
    }

}
