import configuration.ChartConfig;
import configuration.traces.AreaTraceConfig;
import configuration.traces.LineTraceConfig;
import configuration.traces.TraceConfig;
import configuration.traces.TraceType;
import data.datasets.XYData;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame2 extends JFrame {

    public MainFrame2() throws HeadlessException {
        int width = 500;
        int height = 500;

        setTitle("Test chart");

        double[] yData1 = new double[150];
        Random rand = new Random();
        for (int i = 0; i < yData1.length ; i++) {
            yData1[i] = new Double(rand.nextInt(80));
        }

        int[] yData2 = new int[100];
        for (int i = 0; i < yData2.length ; i++) {
            yData2[i] = - i;
        }

        XYData xyData1 = new XYData();
        xyData1.setYData(yData1);
        TraceConfig trace1 = new LineTraceConfig(xyData1);

        XYData xyData2 = new XYData();
        xyData2.setYData(yData2);
        TraceConfig trace2 = new AreaTraceConfig(xyData2);

        ChartConfig chartConfig = new ChartConfig(ChartConfig.DEBUG_THEME, width, height);
        chartConfig.addTrace(trace1, true, true);
        chartConfig.addStack(5);
        chartConfig.addTrace(trace2, true, true);

        Chart chart = new Chart(chartConfig);
        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(width, height));
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
