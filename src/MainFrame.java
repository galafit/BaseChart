import axis.AxisType;
import data.DataList;
import data.XYList;
import functions.DoubleFunction;
import functions.Sin;
import functions.Tg;
import graphs.AreaGraph;
import graphs.Graph;
import graphs.LineGraph;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {

        setTitle("Test chart");

        XYList<Double> xyList2 = new XYList<Double>();
        for (int i = 0; i <6 ; i++) {
            xyList2.addPoint(4057.0789,new Double(i));
        }

        DataList<Double> periodicData = new DataList<Double>();
        Random rand = new Random();
        for (int i = -3501; i <15000 ; i++) {
            periodicData.addData(rand.nextInt(100)*100.0);
        }


        XYList<Double> xyList = new XYList<Double>();
        for (int i = -1000; i <17000 ; i++) {
            // xyList.addPoint(i,rand.nextDouble() * 130);
            xyList.addPoint(i,new Double(-i));
        }


       // Function2D foo = new Foo();
       // chart.addGraph(new graphs.LineGraph(), foo);


        DataList<Double> periodicData2 = new DataList<Double>();
        for (int i = 0; i <15000; i++) {
            periodicData2.addData(new Double(i));
        }

        Graph g1 = new LineGraph();
        g1.setData(xyList);

        Graph g2 = new AreaGraph();
        g2.setData(periodicData2);

        DoubleFunction<Double> sin = new Sin();
        Graph g3 = new LineGraph();
        g3.setFunction(sin);


        DoubleFunction<Double> tg = new Tg();
        Graph g4 = new LineGraph();
        g4.setFunction(tg);



        ChartWithPreview chartWithPreview = new ChartWithPreview();

        chartWithPreview.addChart();
        chartWithPreview.addChart();
        chartWithPreview.addPreview();

        chartWithPreview.addGraph(g2, 0);
        chartWithPreview.addGraph(g3, 0);
        chartWithPreview.addGraph(g1, 1);
        //chartWithPreview.addGraph(g4, 1);

        Graph previewGraph = new LineGraph();
        previewGraph.setData(periodicData);

        Graph randomGraph = new LineGraph();
        randomGraph.setData(periodicData);
       // chartWithPreview.addGraph(randomGraph,1);

        chartWithPreview.addPreviewGraph(previewGraph,0);


        PreviewChartPanel chartPanel = new PreviewChartPanel(chartWithPreview);
        //ChartPanel chartPanel = new ChartPanel(chart);

        setPreferredSize(new Dimension(500,500));
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
