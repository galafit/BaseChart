package graphs;


import data.DataProcessorForNumbers;
import data.XYPoint;
import tooltips.TooltipItem;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph<Number> {

    {
       graphPainter = new LineGraphPainter();
       dataProcessor = new DataProcessorForNumbers();
    }
}
