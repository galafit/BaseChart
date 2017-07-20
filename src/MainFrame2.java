import axis.AxisType;
import data.DataList;
import data.DataPointList;
import functions.Sin;
import functions.Tg;
import graphs.Graph;
import graphs.LineGraph;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.function.DoubleFunction;

/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame2 extends JFrame {

    public MainFrame2() throws HeadlessException {

        setTitle("Test chart");

        Chart chart = new Chart();

        chart.addYAxis(AxisType.LINEAR, true);
        // chart.addXAxis(AxisType.LINEAR, true);

        DataList<Double> periodicData = new DataList<Double>();
        Random rand = new Random();
        for (int i = -35; i <150 ; i++) {
            periodicData.addData(new Double(rand.nextInt(100)));
        }

        DataPointList<Double> xyList = new DataPointList<Double>();
        for (int i = -11; i <100 ; i++) {
           // xyList.addPoint(i,rand.nextDouble() * 130);
            xyList.addPoint(i,new Double(i));
        }

        DataPointList<Double> xyList1 = new DataPointList<Double>();
        for (int i = -11; i <100 ; i++) {
            // xyList.addPoint(i,rand.nextDouble() * 130);
            xyList1.addPoint(i,new Double(-i));
        }

        Graph graph2 = new LineGraph();
        graph2.setData(xyList1);
        chart.addGraph(graph2);

        Graph graph1 = new LineGraph();
        graph1.setData(xyList);
        chart.addGraph(graph1);


        DoubleFunction<Double> sin = new Sin();
        Graph g3 = new LineGraph();
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

     /*  for (int j = 0; j < 100; j++) {
            try {
                Thread.sleep(1000);
                for (int i = 0; i < 10; i++) {
                    dataList.addData(new Double(j * 50));
                }
                chartPanel.update();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           try {
               Thread.sleep(1000);
               for (int i = 0; i < 10; i++) {
                   xyList.addPoint(i*30,j * 50.0);
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
