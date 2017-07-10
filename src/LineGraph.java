import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.List;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph {


    public void draw(Graphics2D g, Rectangle area) {
        if (dataItemList == null) {return;}
        g.setColor(color);
        GeneralPath path = new GeneralPath();
        double x = xAxis.valueToPoint(dataItemList.get(0).getX(), area);
        double y = yAxis.valueToPoint(dataItemList.get(0).getY(), area);

        path.moveTo(x, y);
        for (int i = 1; i < dataItemList.size(); i++) {
            x = xAxis.valueToPoint(dataItemList.get(i).getX(), area);
            y = yAxis.valueToPoint(dataItemList.get(i).getY(), area);
            path.lineTo(x, y);
        }
        g.draw(path);
    }

}
