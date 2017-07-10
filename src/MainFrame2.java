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
public class MainFrame2 extends JFrame {

    public MainFrame2() throws HeadlessException {

        setTitle("Test chart");

        Chart chart = new Chart();

        chart.addYAxis(AxisType.LINEAR, true);
        // chart.addXAxis(AxisType.LINEAR, true);

        PeriodicData periodicData = new PeriodicData(-1,1);
        Random rand = new Random();
        for (int i = -35; i <150 ; i++) {
            periodicData.addData(rand.nextInt(100));
        }

        XYList xyList = new XYList();
        for (int i = -11; i <100 ; i++) {
            xyList.addItem(i,rand.nextDouble() * 130);
        }
        chart.addGraph(new LineGraph(),xyList);

        chart.addGraph(new LineGraph(),periodicData);
        //chart.addGraph(new LineGraph(), xyList2, 1,1);

      /*  Function2D foo = new Foo();
        chart.addGraph(new LineGraph(), foo); */



        Function2D sin = new Sin();
      //  chart.addGraph(new LineGraph(),sin);

        Function2D tg = new Tg();
        //chart1.addGraph(new LineGraph(), tg);


        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(500, 500));
        chartPanel.setBackground(Color.BLACK);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        add(chartPanel,BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

       for (int j = 0; j < 100; j++) {
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
                   xyList.addItem(i*30,j * 50);
               }
               chartPanel.update();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }




    }


    public static void main(String[] args) {
        new MainFrame2();
    }

}
