package graphs;

import data.DataProcessorForNumbers;
import data.XYPoint;
import tooltips.TooltipInfo;
import tooltips.TooltipItem;

/**
 * Created by hdablin on 26.04.17.
 */
public class AreaGraph extends Graph<Number> {
    {
        graphPainter = new AreaGraphPainter();
        dataProcessor = new DataProcessorForNumbers();
    }

    @Override
    public TooltipItem getTooltipItem() {
        XYPoint<Number> hoverPoint = graphPainter.getHoverPoint();
        if (hoverPoint == null){
            return null;
        }
        return new TooltipItem("point y", hoverPoint.getY().toString(), graphPainter.getSettings().getLineColor());
    }
}
