import main.chart.axis.AxisType;
import main.chart.functions.Function2D;
import main.chart.functions.Sin;
import main.chart.functions.Tg;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {

        setTitle("Test chart");



        Chart chart = new Chart();


        XYList xyList2 = new XYList();
        for (int i = 0; i <6 ; i++) {
            xyList2.addItem(4057.0789,i);
        }


        chart.addYAxis(AxisType.LINEAR, true);
       // chart.addXAxis(AxisType.LINEAR, true);

        PeriodicData periodicData = new PeriodicData(-100,1);
        Random rand = new Random();
        for (int i = -3501; i <15000 ; i++) {
            periodicData.addData(rand.nextInt(100)*100.0);
        }




        chart.addGraph(new LineGraph(),periodicData);

       // Function2D foo = new Foo();
       // chart.addGraph(new LineGraph(), foo);


        PeriodicData periodicData2 = new PeriodicData(0,1);
        for (int i = 0; i <15000; i++) {
            periodicData2.addData(i);
        }


        Chart chart1 = new Chart();
        chart1.addGraph(new AreaGraph(),periodicData2);
        Function2D sin = new Sin();
        chart1.addGraph(new LineGraph(),sin);

        Function2D tg = new Tg();
        //chart1.addGraph(new LineGraph(), tg);


        ChartWithPreview chartWithPreview = new ChartWithPreview();

        chartWithPreview.addChart(chart1);
        chartWithPreview.addChart(chart);
        chartWithPreview.addPreviewPanel();

        chartWithPreview.addPreviewGraph(new LineGraph(),periodicData2,0);
        chartWithPreview.addPreviewGraph(new LineGraph(),periodicData,0);






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
