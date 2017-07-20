package data;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by galafit on 15/7/17.
 */
public class XYList<Y> implements XYSet<Y> {
    private ArrayList<XYPoint<Y>> points = new ArrayList<XYPoint<Y>>();

    public XYList() {
    }

    public XYList(ArrayList<XYPoint<Y>> points) {
        this.points = points;
    }

    public void addPoint(double x, Y y) {
        points.add(new XYPoint<Y>(x, y));
    }

    @Override
    public long size() {
        return points.size();
    }

    @Override
    public Double getX(long index) {
        if(index > Integer.MAX_VALUE) {
            String errorMessage = "Illegal index. Index = {0}.  Expected <= {1}";
            String formattedError = MessageFormat.format(errorMessage,index, Integer.MAX_VALUE);
            throw new IllegalArgumentException(formattedError);
        }
        return points.get((int) index).getX();
    }

    @Override
    public Y getY(long index) {
        if(index > Integer.MAX_VALUE) {
            String errorMessage = "Illegal index. Index = {0}.  Expected <= {1}";
            String formattedError = MessageFormat.format(errorMessage,index, Integer.MAX_VALUE);
            throw new IllegalArgumentException(formattedError);
        }
        return points.get((int) index).getY();
    }
}
