import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph {

    @Override
    public void draw(Graphics2D g, Rectangle area) {
        g.setColor(color);

        GeneralPath path = new GeneralPath();
        double x_0 = xAxis.valueToPoint(dataItemList.get(0).getX(), area);
        double y_0 = yAxis.valueToPoint(dataItemList.get(0).getY(), area);

        double x = x_0;
        double y = y_0;
        path.moveTo(x, y);
        for (int i = 1; i < dataItemList.size(); i++) {
            x = xAxis.valueToPoint(dataItemList.get(i).getX(), area);
            y = yAxis.valueToPoint(dataItemList.get(i).getY(), area);
            path.lineTo(x, y);
        }
        g.draw(path);

        path.lineTo(x, area.getY() + area.getHeight());
        path.lineTo(x_0, area.getY() + area.getHeight());
        path.lineTo(x_0, y_0);
        Color transparentColor =new Color(color.getRed(), color.getGreen(), color.getBlue(), 100 );
        g.setColor(transparentColor);
        g.fill(path);

    }
}
