package graphs;


import data.DataProcessorForDoubles;
import data.XYPoint;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph<Double> {
    {
       graphPainter = new LineGraphPainter();
       dataProcessor = new DataProcessorForDoubles();
    }

    @Override
    public String getTooltipText() {
         XYPoint hoverPoint = graphPainter.getHoverPoint();

         if (hoverPoint == null){
             return "";
         }
        String xString =  "x = " + hoverPoint.getX();
        String yString =  "y = " + hoverPoint.getY();
        return "<html> <font color=\"red\">"+xString + "<br>" + yString+"</font></html>";
    }
}
