import axis.AxisType;
import data.DataList;
import data.XYList;
import functions.Sin;
import functions.Tg;
import graphs.AreaGraph;
import graphs.Graph;
import graphs.LineGraph;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.function.DoubleFunction;

/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {

        setTitle("Test chart");



        Chart chart = new Chart();


        XYList<Double> xyList2 = new XYList<Double>();
        for (int i = 0; i <6 ; i++) {
            xyList2.addPoint(4057.0789,new Double(i));
        }


        chart.addYAxis(AxisType.LINEAR, true);
       // chart.addXAxis(AxisType.LINEAR, true);

        DataList<Double> periodicData = new DataList<>();
        Random rand = new Random();
        for (int i = -3501; i <15000 ; i++) {
            periodicData.addData(rand.nextInt(100)*100.0);
        }


        XYList<Double> xyList = new XYList<Double>();
        for (int i = -11; i <18000 ; i++) {
            // xyList.addPoint(i,rand.nextDouble() * 130);
            xyList.addPoint(i,new Double(-i));
        }



        Graph g1 = new LineGraph();
        g1.setData(xyList);

        chart.addGraph(g1);

       // Function2D foo = new Foo();
       // chart.addGraph(new graphs.LineGraph(), foo);


        DataList<Double> periodicData2 = new DataList<Double>();
        for (int i = 0; i <15000; i++) {
            periodicData2.addData(new Double(i));
        }


        Chart chart1 = new Chart();
        Graph g2 = new AreaGraph();
        g2.setData(periodicData2);
        chart1.addGraph(g2);

        DoubleFunction<Double> sin = new Sin();
        Graph g3 = new LineGraph();
        g3.setFunction(sin);
        chart1.addGraph(g3);

        DoubleFunction<Double> tg = new Tg();
        Graph g4 = new LineGraph();
        g4.setFunction(sin);
        //chart1.addGraph(g4);


        ChartWithPreview chartWithPreview = new ChartWithPreview();

        chartWithPreview.addChart(chart1);
        chartWithPreview.addChart(chart);
        chartWithPreview.addPreviewPanel();

       // chartWithPreview.addPreviewGraph(new graphs.LineGraph(),periodicData2,0);
       // chartWithPreview.addPreviewGraph(new graphs.LineGraph(),periodicData,0);






        PreviewChartPanel chartPanel = new PreviewChartPanel(chartWithPreview);
        //ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(500, 500));
        chartPanel.setBackground(Color.BLACK);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        add(chartPanel,BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

     /*   for (int j = 0; j < 100; j++) {
            try {
                Thread.sleep(1000);
                for (int i = 0; i < 10; i++) {
                    periodicData.addData(j * 50);
                }
                chartPanel.update();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
                for (int i = 0; i < 10; i++) {
                    periodicData2.addData(i*30);
                }
                chartPanel.update();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }


    public static void main(String[] args) {
        new MainFrame();
    }

    }
