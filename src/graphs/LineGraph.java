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
    public TooltipInfo getTooltipInfo() {
         XYPoint<Double> hoverPoint = graphPainter.getHoverPoint();

         if (hoverPoint == null){
             return null;
         }
        String xString =  "x = " + hoverPoint.getX();
        String yString =  "y = " + hoverPoint.getY();
        String tooltipString = xString + "\n" + yString;
        return new TooltipInfo(tooltipString,hoverPoint.getX(),hoverPoint.getY());
    }
}
