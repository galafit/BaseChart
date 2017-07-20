package graphs;


import data.DataProcessorForDoubles;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph<Double> {
    {
       graphPainter = new LineGraphPainter();
       dataProcessor = new DataProcessorForDoubles();
    }
}
