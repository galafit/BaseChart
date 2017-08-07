package graphs;


import data.DataProcessorForNumbers;
import data.XYPoint;
import tooltips.TooltipInfo;
import tooltips.TooltipItem;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph<Number> {

    {
       graphPainter = new LineGraphPainter();
       dataProcessor = new DataProcessorForNumbers();
    }

    @Override
    public TooltipItem getTooltipItem() {
         XYPoint<Number> hoverPoint = graphPainter.getHoverPoint();
         if (hoverPoint == null){
             return null;
         }
        return new TooltipItem("y = ", hoverPoint.getY().toString(), graphPainter.getSettings().getLineColor());
    }
}
