package graphs;


import data_old.DataProcessorForNumbers;

/**
 * Created by hdablin on 05.04.17.
 */
public class LineGraph extends Graph<Number> {

    {
       graphPainter = new LineGraphPainter();
       dataProcessor = new DataProcessorForNumbers();
    }
}
