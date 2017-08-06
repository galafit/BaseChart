package graphs;


import data.DataProcessorForNumbers;
import data.XYPoint;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph<Number> {
    {
       graphPainter = new LineGraphPainter();
       dataProcessor = new DataProcessorForNumbers();
    }

    @Override
    public TooltipInfo getTooltipInfo() {
         XYPoint<Number> hoverPoint = graphPainter.getHoverPoint();

         if (hoverPoint == null){
             return null;
         }
        String xString =  "x = " + hoverPoint.getX();
        String yString =  "y = " + hoverPoint.getY();
        String tooltipString = xString + "<br>" + yString;
        return new TooltipInfo(tooltipString,hoverPoint.getX(),hoverPoint.getY());
    }
}
