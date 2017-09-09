import axis.AxisType;
import configuration.ChartConfig;
import data.DataList;
import data.XYList;
import functions.DoubleFunction;
import functions.Sin;
import functions.Tg;
import graphs.Graph;
import graphs.LineGraph;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame2 extends JFrame {

    public MainFrame2() throws HeadlessException {

        setTitle("Test chart");

        ChartConfig chartConfig = new ChartConfig(ChartConfig.DEBUG_THEME);
        chartConfig.addYAxis(true);
        // chartConfig.addXAxis(true);
        Chart chart = new Chart(chartConfig);

        DataList<Double> periodicData = new DataList<Double>();
        Random rand = new Random();
        for (int i = -40; i < 150 ; i++) {
            periodicData.addData(new Double(rand.nextInt(80)));
        }

        XYList<Double> xyList = new XYList<Double>();
        for (int i = -50; i < 60 ; i++) {
           // xyList.addPoint(i,rand.nextDouble() * 130);
            xyList.addPoint(i,new Double(i));
        }

        XYList<Double> xyList1 = new XYList<Double>();
        for (int i = -11; i < 60 ; i++) {
            // xyList.addPoint(i,rand.nextDouble() * 130);
            xyList1.addPoint(i,new Double(-i));
        }

        Graph graph1 = new LineGraph();
        graph1.setData(xyList);
        graph1.setGraphName("Graph1");
        chart.addGraph(graph1);

        Graph graph2 = new LineGraph();
        graph2.setData(xyList1);
        graph2.setGraphName("Graph2 with very long name");
        chart.addGraph(graph2);


        DoubleFunction<Double> sin = new Sin();
        Graph g3 = new LineGraph();
        g3.setGraphName("Graph3 sin");
        g3.setFunction(sin);
        chart.addGraph(g3, 0, 1);

        DoubleFunction<Double> tg = new Tg();
        //chart1.addGraph(new graphs.LineGraph(), tg);


        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(500, 500));
        chartPanel.setBackground(Color.BLACK);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        add(chartPanel,BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

       /* try {
            Thread.sleep(3000);
            xyList.addPoint(150 , 150.0);
            chartPanel.update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

      /* for (int j = 0; j < 10; j++) {
           try {
               Thread.sleep(1000);
               for (int i = 0; i < 10; i++) {
                   xyList.addPoint(150 + j*30,(i + j) * 50.0);
               }
               chartPanel.update();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }*/

    }


    public static void main(String[] args) {
        new MainFrame2();
    }

}
