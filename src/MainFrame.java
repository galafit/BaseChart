import base.XYData;
import base.config.ChartConfig;
import base.config.traces.AreaTraceConfig;
import base.config.traces.LineTraceConfig;
import base.config.traces.TraceConfig;
import data.Data;
import data.DataSet;
import data.XYDataSet;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {
        int width = 500;
        int height = 500;

        setTitle("Test chart");

        double[] yData1 = new double[1500];
        Random rand = new Random();
        for (int i = 0; i < yData1.length ; i++) {
            yData1[i] = new Double(rand.nextInt(500));
        }

        int[] yData2 = new int[500];
        for (int i = 0; i < yData2.length ; i++) {
            yData2[i] =  i;
        }

        XYDataSet xyData1 = new XYDataSet();
        xyData1.setYData(yData1);
        TraceConfig trace1 = new LineTraceConfig(xyData1);

        XYDataSet xyData2 = new XYDataSet();
        xyData2.setYData(yData2);
        TraceConfig trace2 = new AreaTraceConfig(xyData2);

        Config config = new Config();
        config.addTrace(trace1);
        config.addStack(5);
        config.addTrace(trace2);

        TraceConfig trace3 = new LineTraceConfig(xyData2);
        TraceConfig trace4 = new LineTraceConfig(xyData1);
        config.addPreviewTrace(trace4);
        config.addPreviewTrace(trace3);

        config.enablePreview(6000);

        Chart chart = new Chart(config, width, height);

        //base.chart.BaseChartWithPreview chart = new base.chart.BaseChartWithPreview(chartConfig);

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
        new MainFrame();
    }

}
